package com.armorcode.runbook;

import java.util.HashMap;
import java.util.Map;

/**
 * Secret Manager class with hardcoded secrets for testing GitHub Secret Scanning
 * This class demonstrates various types of secrets that should be detected
 */
public class SecretManager {
    
    // GitHub Personal Access Tokens
    private static final String GITHUB_PAT = "ghp_1234567890abcdef1234567890abcdef123456";
    private static final String GITHUB_OAUTH_TOKEN = "gho_1234567890abcdef1234567890abcdef123456";
    private static final String GITHUB_APP_TOKEN = "ghs_1234567890abcdef1234567890abcdef123456";
    
    // AWS Credentials
    private static final String AWS_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE";
    private static final String AWS_SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
    
    // Database passwords
    private static final String DB_PASSWORD = "MySecretDatabasePassword2024!";
    private static final String REDIS_PASSWORD = "RedisSecretPassword123!";
    
    // API Keys
    private static final String STRIPE_SECRET_KEY = "sk_live_1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef";
    private static final String STRIPE_TEST_KEY = "sk_test_4eC39HqLyjWDarjtT1zdp7dc";
    private static final String OPENAI_API_KEY = "sk-1234567890abcdef1234567890abcdef1234567890abcdef1234";
    private static final String SENDGRID_API_KEY = "SG.1234567890abcdef.1234567890abcdef1234567890abcdef1234567890abcdef";
    
    // OAuth Secrets
    private static final String GOOGLE_CLIENT_SECRET = "GOCSPX-1234567890abcdef1234567890abcdef12";
    private static final String FACEBOOK_APP_SECRET = "1234567890abcdef1234567890abcdef";
    private static final String TWITTER_CONSUMER_SECRET = "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef";
    
    // Slack tokens
    private static final String SLACK_BOT_TOKEN = "xoxb-1234567890-1234567890-abcdefghijklmnopqrstuvwx";
    private static final String SLACK_USER_TOKEN = "xoxp-1234567890-1234567890-1234567890-abcdefghijklmnopqrstuvwx";
    private static final String SLACK_WEBHOOK = "https://hooks.slack.com/services/T00000000/B00000000/XXXXXXXXXXXXXXXXXXXXXXXX";
    
    // JWT and encryption keys
    private static final String JWT_SECRET = "myJWTSecretKey1234567890abcdef1234567890abcdef1234567890abcdef";
    private static final String ENCRYPTION_KEY = "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef12";
    
    // Docker and NPM tokens
    private static final String DOCKER_PAT = "dckr_pat_1234567890abcdef1234567890abcdef1234567890abcdef";
    private static final String NPM_TOKEN = "npm_1234567890abcdef1234567890abcdef12345678";
    
    // Private keys (RSA)
    private static final String RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEpAIBAAKCAQEA1234567890abcdef1234567890abcdef1234567890abcdef\n" +
            "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef12\n" +
            "-----END RSA PRIVATE KEY-----";
    
    // SSH Private Key
    private static final String SSH_PRIVATE_KEY = "-----BEGIN OPENSSH PRIVATE KEY-----\n" +
            "b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAFwAAAAdzc2gtcn\n" +
            "NhAAAAAwEAAQAAAQEA1234567890abcdef1234567890abcdef1234567890abcdef1234\n" +
            "-----END OPENSSH PRIVATE KEY-----";
    
    // Azure secrets
    private static final String AZURE_CLIENT_SECRET = "1234567890abcdef1234567890abcdef12345678";
    private static final String AZURE_TENANT_ID = "12345678-1234-1234-1234-123456789012";
    
    // Webhook secrets
    private static final String WEBHOOK_SECRET = "whsec_1234567890abcdef1234567890abcdef1234567890abcdef";
    private static final String DISCORD_WEBHOOK = "https://discord.com/api/webhooks/123456789012345678/abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ123456";
    
    /**
     * Get all secrets as a map (for testing purposes only)
     */
    public Map<String, String> getAllSecrets() {
        Map<String, String> secrets = new HashMap<>();
        secrets.put("github_pat", GITHUB_PAT);
        secrets.put("aws_access_key", AWS_ACCESS_KEY);
        secrets.put("aws_secret_key", AWS_SECRET_KEY);
        secrets.put("db_password", DB_PASSWORD);
        secrets.put("stripe_key", STRIPE_SECRET_KEY);
        secrets.put("openai_key", OPENAI_API_KEY);
        secrets.put("jwt_secret", JWT_SECRET);
        secrets.put("slack_token", SLACK_BOT_TOKEN);
        return secrets;
    }
    
    /**
     * Method that uses hardcoded API key in URL
     */
    public String buildApiUrl() {
        return "https://api.example.com/v1/data?api_key=abc123def456ghi789jkl012mno345pqr678stu901vwx234yz";
    }
    
    /**
     * Method with embedded connection string
     */
    public String getDatabaseConnectionString() {
        return "postgresql://admin:MySecretDbPassword2024!@localhost:5432/testdb";
    }
}
