# GitHub App Integration Guide: Fetching anthem.toml Files

This guide explains how to dynamically fetch all `anthem.toml` files from customer repositories using a GitHub App with read access to all repositories in an organization.

## Overview

When working with customer organizations, you'll need to:
1. Create and install a GitHub App in the customer's organization
2. Use the GitHub App to authenticate and access repositories
3. Search for and fetch all `anthem.toml` files across repositories
4. Parse and process the configuration data

## Prerequisites

- GitHub App installed in customer organization with **Repository Contents: Read** permission
- GitHub App private key (PEM format)
- GitHub App ID
- Installation ID for the customer organization

## Authentication Flow

### 1. GitHub App Authentication

```bash
# Required environment variables
GITHUB_APP_ID=123456
GITHUB_APP_PRIVATE_KEY_PATH=/path/to/private-key.pem
GITHUB_INSTALLATION_ID=12345678
```

### 2. Generate JWT Token

The GitHub App uses JWT (JSON Web Token) for authentication:

```java
// Example JWT generation (you'll need a JWT library)
String jwt = generateJWT(appId, privateKey);
```

### 3. Get Installation Access Token

Exchange JWT for an installation access token:

```http
POST https://api.github.com/app/installations/{installation_id}/access_tokens
Authorization: Bearer {jwt_token}
```

## Implementation Approaches

### Approach 1: GitHub REST API

#### Step 1: List All Repositories in Organization

```http
GET https://api.github.com/installation/repositories
Authorization: token {installation_access_token}
```

#### Step 2: Search for anthem.toml Files

For each repository, search for `anthem.toml` files:

```http
GET https://api.github.com/repos/{owner}/{repo}/contents
Authorization: token {installation_access_token}
```

Or use the search API:

```http
GET https://api.github.com/search/code?q=filename:anthem.toml+repo:{owner}/{repo}
Authorization: token {installation_access_token}
```

#### Step 3: Fetch File Contents

```http
GET https://api.github.com/repos/{owner}/{repo}/contents/{path}/anthem.toml
Authorization: token {installation_access_token}
```

### Approach 2: GitHub GraphQL API (Recommended)

More efficient for bulk operations:

```graphql
query GetAnthemFiles($org: String!, $cursor: String) {
  organization(login: $org) {
    repositories(first: 100, after: $cursor) {
      pageInfo {
        hasNextPage
        endCursor
      }
      nodes {
        name
        defaultBranchRef {
          target {
            ... on Commit {
              tree {
                entries {
                  name
                  type
                  object {
                    ... on Tree {
                      entries {
                        name
                        type
                        path
                        object {
                          ... on Blob {
                            text
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
```

## Java Implementation Example

### Dependencies (add to pom.xml)

```xml
<dependencies>
    <!-- GitHub API -->
    <dependency>
        <groupId>org.kohsuke</groupId>
        <artifactId>github-api</artifactId>
        <version>1.316</version>
    </dependency>
    
    <!-- JWT for GitHub App authentication -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
    </dependency>
    
    <!-- TOML parsing -->
    <dependency>
        <groupId>com.moandjiezana.toml</groupId>
        <artifactId>toml4j</artifactId>
        <version>0.7.2</version>
    </dependency>
</dependencies>
```

### GitHub App Service Implementation

```java
package com.armorcode.runbook;

import com.moandjiezana.toml.Toml;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.kohsuke.github.*;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.*;

public class GitHubAppAnthemService {
    
    private final String appId;
    private final PrivateKey privateKey;
    private final String installationId;
    
    public GitHubAppAnthemService(String appId, String privateKeyPath, String installationId) 
            throws Exception {
        this.appId = appId;
        this.privateKey = loadPrivateKey(privateKeyPath);
        this.installationId = installationId;
    }
    
    /**
     * Fetch all anthem.toml files from all repositories in the organization
     */
    public Map<String, List<AnthemConfig>> fetchAllAnthemFiles() throws Exception {
        GitHub github = authenticateAsApp();
        Map<String, List<AnthemConfig>> results = new HashMap<>();
        
        // Get all repositories accessible to the installation
        GHAppInstallation installation = github.getApp().getInstallationById(Long.parseLong(installationId));
        
        for (GHRepository repo : installation.listRepositories()) {
            List<AnthemConfig> anthemFiles = findAnthemFilesInRepository(repo);
            if (!anthemFiles.isEmpty()) {
                results.put(repo.getFullName(), anthemFiles);
            }
        }
        
        return results;
    }
    
    /**
     * Find all anthem.toml files in a specific repository
     */
    private List<AnthemConfig> findAnthemFilesInRepository(GHRepository repo) {
        List<AnthemConfig> anthemConfigs = new ArrayList<>();
        
        try {
            // Search for anthem.toml files using GitHub search
            PagedSearchIterable<GHContent> searchResults = repo.searchContent()
                .filename("anthem.toml")
                .list();
            
            for (GHContent content : searchResults) {
                try {
                    String fileContent = content.getContent();
                    Toml toml = new Toml().read(fileContent);
                    
                    AnthemConfig config = new AnthemConfig();
                    config.setRepositoryName(repo.getFullName());
                    config.setFilePath(content.getPath());
                    config.setContent(fileContent);
                    config.setParsedToml(toml);
                    
                    // Extract common fields
                    if (toml.contains("project.name")) {
                        config.setProjectName(toml.getString("project.name"));
                    }
                    if (toml.contains("tool.anthem.owners")) {
                        config.setOwners(toml.getList("tool.anthem.owners"));
                    }
                    
                    anthemConfigs.add(config);
                    
                } catch (Exception e) {
                    System.err.println("Error processing anthem.toml in " + repo.getFullName() + 
                                     " at " + content.getPath() + ": " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error searching for anthem.toml files in " + repo.getFullName() + 
                             ": " + e.getMessage());
        }
        
        return anthemConfigs;
    }
    
    /**
     * Authenticate as GitHub App and get installation token
     */
    private GitHub authenticateAsApp() throws Exception {
        // Generate JWT
        String jwt = generateJWT();
        
        // Create GitHub instance with JWT
        GitHub github = new GitHubBuilder()
            .withJwtToken(jwt)
            .build();
        
        // Get installation access token
        GHAppInstallation installation = github.getApp().getInstallationById(Long.parseLong(installationId));
        GHAppInstallationToken token = installation.createToken().create();
        
        // Return GitHub instance with installation token
        return new GitHubBuilder()
            .withAppInstallationToken(token.getToken())
            .build();
    }
    
    /**
     * Generate JWT token for GitHub App authentication
     */
    private String generateJWT() {
        Instant now = Instant.now();
        
        return Jwts.builder()
            .setIssuer(appId)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(600))) // 10 minutes
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact();
    }
    
    /**
     * Load private key from PEM file
     */
    private PrivateKey loadPrivateKey(String privateKeyPath) throws Exception {
        String privateKeyContent = new String(Files.readAllBytes(new File(privateKeyPath).toPath()));
        
        // Remove PEM headers and whitespace
        privateKeyContent = privateKeyContent
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("-----BEGIN RSA PRIVATE KEY-----", "")
            .replace("-----END RSA PRIVATE KEY-----", "")
            .replaceAll("\\s", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        
        return keyFactory.generatePrivate(spec);
    }
    
    /**
     * Data class for anthem configuration
     */
    public static class AnthemConfig {
        private String repositoryName;
        private String filePath;
        private String content;
        private String projectName;
        private List<String> owners;
        private Toml parsedToml;
        
        // Getters and setters
        public String getRepositoryName() { return repositoryName; }
        public void setRepositoryName(String repositoryName) { this.repositoryName = repositoryName; }
        
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public String getProjectName() { return projectName; }
        public void setProjectName(String projectName) { this.projectName = projectName; }
        
        public List<String> getOwners() { return owners; }
        public void setOwners(List<String> owners) { this.owners = owners; }
        
        public Toml getParsedToml() { return parsedToml; }
        public void setParsedToml(Toml parsedToml) { this.parsedToml = parsedToml; }
        
        @Override
        public String toString() {
            return String.format("AnthemConfig{repo='%s', path='%s', project='%s', owners=%s}", 
                repositoryName, filePath, projectName, owners);
        }
    }
}
```

## Usage Example

```java
public class AnthemFetcherExample {
    public static void main(String[] args) {
        try {
            // Initialize the service
            GitHubAppAnthemService service = new GitHubAppAnthemService(
                "123456",                           // GitHub App ID
                "/path/to/private-key.pem",        // Private key path
                "12345678"                         // Installation ID
            );
            
            // Fetch all anthem.toml files
            Map<String, List<GitHubAppAnthemService.AnthemConfig>> results = 
                service.fetchAllAnthemFiles();
            
            // Process results
            for (Map.Entry<String, List<GitHubAppAnthemService.AnthemConfig>> entry : results.entrySet()) {
                String repoName = entry.getKey();
                List<GitHubAppAnthemService.AnthemConfig> configs = entry.getValue();
                
                System.out.println("Repository: " + repoName);
                for (GitHubAppAnthemService.AnthemConfig config : configs) {
                    System.out.println("  - " + config.getFilePath());
                    System.out.println("    Project: " + config.getProjectName());
                    System.out.println("    Owners: " + config.getOwners());
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Environment Configuration

Create a `.env` file or set environment variables:

```bash
# GitHub App Configuration
GITHUB_APP_ID=123456
GITHUB_APP_PRIVATE_KEY_PATH=/path/to/private-key.pem
GITHUB_INSTALLATION_ID=12345678

# Optional: Specific organization
GITHUB_ORG=customer-org-name
```

## Rate Limiting Considerations

- GitHub API has rate limits (5000 requests/hour for authenticated requests)
- Use pagination for large organizations
- Implement exponential backoff for rate limit handling
- Consider caching results for frequently accessed data

## Security Best Practices

1. **Store private keys securely** (use environment variables or secure vaults)
2. **Rotate access tokens regularly**
3. **Use minimal required permissions**
4. **Log access for audit purposes**
5. **Validate and sanitize TOML content**

## Error Handling

- Handle network timeouts
- Manage API rate limits
- Deal with malformed TOML files
- Handle repository access permissions
- Log errors for debugging

## Monitoring and Logging

```java
// Add logging for monitoring
logger.info("Fetching anthem.toml files from {} repositories", repoCount);
logger.debug("Processing repository: {}", repo.getFullName());
logger.warn("Failed to parse anthem.toml in {}: {}", filePath, error);
```

## Next Steps

1. Set up GitHub App in customer organization
2. Implement the service in your application
3. Add error handling and logging
4. Test with customer repositories
5. Set up monitoring and alerting
6. Document the integration process

This implementation provides a robust foundation for fetching and processing `anthem.toml` files from customer repositories using GitHub App authentication.
