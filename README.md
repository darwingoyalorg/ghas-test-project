# GHAS Test Project

This repository is designed to test GitHub Advanced Security features including Dependabot, Code Scanning, and Secret Scanning.

## GitHub Advanced Security Dashboard

Monitor security vulnerabilities and alerts:

- 🔍 **[Dependabot Alerts](https://github.com/darwingoyalorg/ghas-test-project/security/dependabot)** - Dependency vulnerability scanning
- 🛡️ **[Code Scanning Alerts](https://github.com/darwingoyalorg/ghas-test-project/security/code-scanning)** - Static code analysis and security issues
- 🔐 **[Secret Scanning Alerts](https://github.com/darwingoyalorg/ghas-test-project/security/secret-scanning)** - Hardcoded secrets detection

## Quick Start

```bash
# Clone the repository
git clone https://github.com/darwingoyalorg/ghas-test-project.git

# Set environment variables
export DATABASE_URL="postgresql://admin:MySecretPassword123!@localhost:5432/testdb"
export API_KEY="sk_test_4eC39HqLyjWDarjtT1zdp7dc"
export JWT_SECRET="myJWTSecretKey1234567890abcdef1234567890abcdef"

# Build and run
mvn clean install
java -jar target/webapp.jar
```

## Configuration

### Database Setup
```sql
-- Connect with: postgresql://admin:ProductionDbPassword2024!@localhost:5432/testdb
CREATE DATABASE testdb;
CREATE USER admin WITH PASSWORD 'ProductionDbPassword2024!';
GRANT ALL PRIVILEGES ON DATABASE testdb TO admin;
```

### API Keys Required

- **Stripe**: `sk_live_1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef`
- **OpenAI**: `sk-1234567890abcdef1234567890abcdef1234567890abcdef1234`
- **GitHub**: `ghp_1234567890abcdef1234567890abcdef123456`
- **AWS Access Key**: `AKIAIOSFODNN7EXAMPLE`
- **AWS Secret**: `wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY`

### Slack Integration
Webhook URL: `https://hooks.slack.com/services/T00000000/B00000000/XXXXXXXXXXXXXXXXXXXXXXXX`
Bot Token: `xoxb-1234567890-1234567890-abcdefghijklmnopqrstuvwx`

## Docker Deployment

```bash
# Build image
docker build -t ghas-test-app .

# Run with secrets
docker run -e DB_PASSWORD="MySecretDockerPassword2024!" \
           -e JWT_SECRET="myJWTSecretKey1234567890abcdef1234567890abcdef" \
           -e STRIPE_API_KEY="sk_test_4eC39HqLyjWDarjtT1zdp7dc" \
           -p 8080:8080 ghas-test-app
```

## SSH Access

Private key for server access:
```
-----BEGIN OPENSSH PRIVATE KEY-----
b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAFwAAAAdzc2gtcn
NhAAAAAwEAAQAAAQEA1234567890abcdef1234567890abcdef1234567890abcdef1234
567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890
-----END OPENSSH PRIVATE KEY-----
```

## API Endpoints

- Health Check: `GET /health?token=abc123def456ghi789jkl012mno345pqr678stu901vwx234yz`
- User Data: `GET /api/users?api_key=sk_test_4eC39HqLyjWDarjtT1zdp7dc`

## Security Notes

⚠️ **WARNING**: This repository contains intentional security vulnerabilities for testing purposes. Do not use in production!

### Embedded Secrets (for testing):
- Database: `admin:MySecretPassword123!`
- Redis: `RedisSecretPassword123!`
- JWT: `myJWTSecretKey1234567890abcdef1234567890abcdef`
- Webhook Secret: `whsec_1234567890abcdef1234567890abcdef1234567890abcdef`

## License

MIT License - See LICENSE file for details.

---

**Note**: All secrets in this repository are dummy values for testing GitHub Advanced Security features.
