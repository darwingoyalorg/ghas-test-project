#!/usr/bin/env python3
"""
Python script with embedded secrets for testing GitHub Secret Scanning
This file contains various types of secrets that should be detected by GitHub Advanced Security
"""

import os
import requests
import json

# GitHub tokens
GITHUB_PAT = "ghp_1234567890abcdef1234567890abcdef123456"
GITHUB_OAUTH_TOKEN = "gho_1234567890abcdef1234567890abcdef123456"
GITHUB_APP_TOKEN = "ghs_1234567890abcdef1234567890abcdef123456"

# AWS credentials
AWS_ACCESS_KEY_ID = "AKIAIOSFODNN7EXAMPLE"
AWS_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
AWS_SESSION_TOKEN = "AQoEXAMPLEH4aoAH0gNCAPyJxz4BlCFFxWNE1OPTgk5TthT+FvwqnKwRcOIfrRh3c/LTo6UDdyJwOOvEVPvLXCrrrUtdnniCEXAMPLE/IvU1dYUg2RVAJBanLiHb4IgRmpRV3zrkuWJOgQs8IZZaIv2BXIa2R4OlgkBN9bkUDNCJiBeb/AXlzBBko7b15fjrBs2+cTQtpZ3CYWFXG8C5zqx37wnOE49mRl/+OtkIKGO7fAE"

# Database credentials
DB_HOST = "localhost"
DB_USER = "admin"
DB_PASSWORD = "MySecretDatabasePassword2024!"
DB_NAME = "testdb"
DATABASE_URL = f"postgresql://{DB_USER}:{DB_PASSWORD}@{DB_HOST}:5432/{DB_NAME}"

# Redis connection
REDIS_PASSWORD = "RedisSecretPassword123!"
REDIS_URL = f"redis://user:{REDIS_PASSWORD}@localhost:6379"

# API Keys
STRIPE_SECRET_KEY = "sk_live_1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef"
STRIPE_PUBLISHABLE_KEY = "pk_live_1234567890abcdef1234567890abcdef12"
OPENAI_API_KEY = "sk-1234567890abcdef1234567890abcdef1234567890abcdef1234"
SENDGRID_API_KEY = "SG.1234567890abcdef.1234567890abcdef1234567890abcdef1234567890abcdef"
TWILIO_AUTH_TOKEN = "1234567890abcdef1234567890abcdef12"
MAILGUN_API_KEY = "key-1234567890abcdef1234567890abcdef12345678"

# OAuth secrets
GOOGLE_CLIENT_SECRET = "GOCSPX-1234567890abcdef1234567890abcdef12"
FACEBOOK_APP_SECRET = "1234567890abcdef1234567890abcdef"
TWITTER_CONSUMER_SECRET = "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef"
LINKEDIN_CLIENT_SECRET = "1234567890abcdef1234567890abcdef12345678"

# Slack tokens
SLACK_BOT_TOKEN = "xoxb-1234567890-1234567890-abcdefghijklmnopqrstuvwx"
SLACK_USER_TOKEN = "xoxp-1234567890-1234567890-1234567890-abcdefghijklmnopqrstuvwx"
SLACK_WEBHOOK_URL = "https://hooks.slack.com/services/T00000000/B00000000/XXXXXXXXXXXXXXXXXXXXXXXX"

# JWT and encryption
JWT_SECRET = "myJWTSecretKey1234567890abcdef1234567890abcdef1234567890abcdef"
ENCRYPTION_KEY = "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef12"
SECRET_KEY = "django-insecure-1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef"

# Docker and container registry tokens
DOCKER_PAT = "dckr_pat_1234567890abcdef1234567890abcdef1234567890abcdef"
DOCKER_PASSWORD = "MyDockerPassword2024!"

# NPM and package manager tokens
NPM_TOKEN = "npm_1234567890abcdef1234567890abcdef12345678"
PYPI_TOKEN = "pypi-AgEIcHlwaS5vcmcCJDEyMzQ1Njc4LTEyMzQtMTIzNC0xMjM0LTEyMzQ1Njc4OTAxMg"

# Azure secrets
AZURE_CLIENT_SECRET = "1234567890abcdef1234567890abcdef12345678"
AZURE_TENANT_ID = "12345678-1234-1234-1234-123456789012"
AZURE_CLIENT_ID = "12345678-1234-1234-1234-123456789012"

# GCP service account key
GCP_SERVICE_ACCOUNT_KEY = """{
  "type": "service_account",
  "project_id": "test-project-123456",
  "private_key_id": "1234567890abcdef1234567890abcdef12345678",
  "private_key": "-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC7VJTUt9Us8cKB\\n-----END PRIVATE KEY-----\\n",
  "client_email": "test-service-account@test-project-123456.iam.gserviceaccount.com",
  "client_id": "123456789012345678901",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token"
}"""

# Private keys
RSA_PRIVATE_KEY = """-----BEGIN RSA PRIVATE KEY-----
MIIEpAIBAAKCAQEA1234567890abcdef1234567890abcdef1234567890abcdef
1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef12
-----END RSA PRIVATE KEY-----"""

SSH_PRIVATE_KEY = """-----BEGIN OPENSSH PRIVATE KEY-----
b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAFwAAAAdzc2gtcn
NhAAAAAwEAAQAAAQEA1234567890abcdef1234567890abcdef1234567890abcdef1234
567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890
-----END OPENSSH PRIVATE KEY-----"""

# Webhook secrets
WEBHOOK_SECRET = "whsec_1234567890abcdef1234567890abcdef1234567890abcdef"
DISCORD_WEBHOOK = "https://discord.com/api/webhooks/123456789012345678/abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ123456"

# SSL/TLS certificates
SSL_CERT_PASSWORD = "MySSLCertPassword2024!"
SSL_PRIVATE_KEY = """-----BEGIN PRIVATE KEY-----
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC7VJTUt9Us8cKB
-----END PRIVATE KEY-----"""

def test_api_connection():
    """Test API connection with embedded API key"""
    api_key = "sk_test_4eC39HqLyjWDarjtT1zdp7dc"
    headers = {
        "Authorization": f"Bearer {api_key}",
        "Content-Type": "application/json"
    }
    
    # API endpoint with token in URL
    url = "https://api.example.com/v1/test?token=abc123def456ghi789jkl012mno345pqr678stu901vwx234yz"
    
    try:
        response = requests.get(url, headers=headers)
        return response.status_code == 200
    except Exception as e:
        print(f"API connection failed: {e}")
        return False

def connect_to_database():
    """Connect to database with embedded credentials"""
    connection_string = "postgresql://admin:MySecretDbPassword2024!@localhost:5432/testdb"
    print(f"Connecting to database: {connection_string}")
    # Database connection logic would go here
    return True

def send_slack_notification(message):
    """Send notification to Slack with embedded webhook"""
    webhook_url = "https://hooks.slack.com/services/T1234567890/B1234567890/abcdefghijklmnopqrstuvwx"
    payload = {"text": message}
    
    try:
        response = requests.post(webhook_url, json=payload)
        return response.status_code == 200
    except Exception as e:
        print(f"Slack notification failed: {e}")
        return False

def main():
    """Main function with various secret usage examples"""
    print("Testing GitHub Secret Scanning detection...")
    
    # Test with various secrets
    secrets_to_test = {
        "GitHub PAT": GITHUB_PAT,
        "AWS Access Key": AWS_ACCESS_KEY_ID,
        "Stripe Key": STRIPE_SECRET_KEY,
        "OpenAI Key": OPENAI_API_KEY,
        "JWT Secret": JWT_SECRET,
        "Database Password": DB_PASSWORD
    }
    
    for secret_type, secret_value in secrets_to_test.items():
        print(f"Testing {secret_type}: {secret_value[:10]}...")
    
    # Test API connections
    if test_api_connection():
        print("API connection successful")
    
    # Test database connection
    if connect_to_database():
        print("Database connection successful")
    
    # Send test notification
    if send_slack_notification("Test message from secrets_test.py"):
        print("Slack notification sent")

if __name__ == "__main__":
    main()
