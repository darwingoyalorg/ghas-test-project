// JavaScript configuration file with secrets for testing GitHub Secret Scanning

// API Configuration
const API_CONFIG = {
    // GitHub tokens
    githubToken: 'ghp_1234567890abcdef1234567890abcdef123456',
    githubOAuthToken: 'gho_1234567890abcdef1234567890abcdef123456',
    
    // AWS credentials
    awsAccessKeyId: 'AKIAIOSFODNN7EXAMPLE',
    awsSecretAccessKey: 'wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY',
    
    // Stripe keys
    stripePublishableKey: 'pk_test_1234567890abcdef1234567890abcdef12',
    stripeSecretKey: 'sk_test_4eC39HqLyjWDarjtT1zdp7dc',
    
    // OpenAI API key
    openaiApiKey: 'sk-1234567890abcdef1234567890abcdef1234567890abcdef1234',
    
    // SendGrid API key
    sendgridApiKey: 'SG.1234567890abcdef.1234567890abcdef1234567890abcdef1234567890abcdef',
    
    // Slack tokens
    slackBotToken: 'xoxb-1234567890-1234567890-abcdefghijklmnopqrstuvwx',
    slackUserToken: 'xoxp-1234567890-1234567890-1234567890-abcdefghijklmnopqrstuvwx',
    slackWebhook: 'https://hooks.slack.com/services/T00000000/B00000000/XXXXXXXXXXXXXXXXXXXXXXXX',
    
    // Database connection
    databaseUrl: 'postgresql://admin:MySecretPassword123!@localhost:5432/testdb',
    
    // JWT secret
    jwtSecret: 'myJWTSecretKey1234567890abcdef1234567890abcdef1234567890abcdef',
    
    // OAuth secrets
    googleClientSecret: 'GOCSPX-1234567890abcdef1234567890abcdef12',
    facebookAppSecret: '1234567890abcdef1234567890abcdef',
    twitterConsumerSecret: '1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef',
    
    // Docker and NPM tokens
    dockerToken: 'dckr_pat_1234567890abcdef1234567890abcdef1234567890abcdef',
    npmToken: 'npm_1234567890abcdef1234567890abcdef12345678',
    
    // Azure secrets
    azureClientSecret: '1234567890abcdef1234567890abcdef12345678',
    azureTenantId: '12345678-1234-1234-1234-123456789012',
    
    // Webhook secrets
    webhookSecret: 'whsec_1234567890abcdef1234567890abcdef1234567890abcdef',
    discordWebhook: 'https://discord.com/api/webhooks/123456789012345678/abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ123456'
};

// Function to initialize API client
function initializeApiClient() {
    const apiKey = 'sk_test_4eC39HqLyjWDarjtT1zdp7dc';
    const baseUrl = 'https://api.example.com/v1';
    
    return {
        headers: {
            'Authorization': `Bearer ${apiKey}`,
            'Content-Type': 'application/json'
        },
        baseUrl: baseUrl
    };
}

// Database connection string
const DB_CONNECTION = 'mongodb://admin:MyMongoPassword2024!@localhost:27017/testdb';

// Redis connection
const REDIS_URL = 'redis://user:RedisSecretPassword123!@localhost:6379';

// Private key for JWT signing
const PRIVATE_KEY = `-----BEGIN RSA PRIVATE KEY-----
MIIEpAIBAAKCAQEA1234567890abcdef1234567890abcdef1234567890abcdef
1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef12
-----END RSA PRIVATE KEY-----`;

// SSH private key
const SSH_PRIVATE_KEY = `-----BEGIN OPENSSH PRIVATE KEY-----
b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAFwAAAAdzc2gtcn
NhAAAAAwEAAQAAAQEA1234567890abcdef1234567890abcdef1234567890abcdef1234
567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890
-----END OPENSSH PRIVATE KEY-----`;

// API endpoints with embedded tokens
const API_ENDPOINTS = {
    users: 'https://api.example.com/users?token=abc123def456ghi789jkl012mno345pqr678stu901vwx234yz',
    payments: 'https://api.stripe.com/v1/charges?api_key=sk_test_4eC39HqLyjWDarjtT1zdp7dc',
    notifications: 'https://api.sendgrid.com/v3/mail/send?api_key=SG.1234567890abcdef.1234567890abcdef1234567890abcdef1234567890abcdef'
};

// Export configuration
if (typeof module !== 'undefined' && module.exports) {
    module.exports = API_CONFIG;
}
