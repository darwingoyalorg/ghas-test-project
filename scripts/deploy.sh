#!/bin/bash

# Deployment script with embedded secrets for testing GitHub Secret Scanning

set -e

echo "Starting deployment process..."

# AWS Configuration
export AWS_ACCESS_KEY_ID="AKIAI44QH8DHBEXAMPLE"
export AWS_SECRET_ACCESS_KEY="je7MtGbClwBF/2Zp9Utk/h3yCo8nvbEXAMPLEKEY"
export AWS_DEFAULT_REGION="us-west-2"

# Database credentials
DB_HOST="production-db.example.com"
DB_USER="admin"
DB_PASS="ProductionDbPassword2024!"

# API Keys
STRIPE_KEY="sk_live_abcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef"
SENDGRID_KEY="SG.abcdef1234567890.abcdef1234567890abcdef1234567890abcdef1234567890abcdef"
OPENAI_KEY="sk-abcdef1234567890abcdef1234567890abcdef1234567890abcdef1234"

# GitHub token for API access
GITHUB_TOKEN="ghp_abcdef1234567890abcdef1234567890abcdef12"

# Docker registry credentials
DOCKER_USERNAME="myuser"
DOCKER_PASSWORD="MyDockerPassword2024!"
DOCKER_REGISTRY_TOKEN="dckr_pat_abcdef1234567890abcdef1234567890abcdef1234567890abcdef"

# Slack webhook for notifications
SLACK_WEBHOOK="https://hooks.slack.com/services/T1234567890/B1234567890/abcdefghijklmnopqrstuvwx"

# JWT secret for token signing
JWT_SECRET="myJWTSecretKey1234567890abcdef1234567890abcdef1234567890abcdef"

# SSL certificate password
SSL_CERT_PASSWORD="MySSLPassword2024!"

echo "Authenticating with Docker registry..."
echo "$DOCKER_PASSWORD" | docker login --username "$DOCKER_USERNAME" --password-stdin

echo "Building application..."
mvn clean package -DskipTests

echo "Building Docker image..."
docker build -t myapp:latest .

echo "Pushing to registry..."
docker push myapp:latest

echo "Deploying to AWS..."
aws ecs update-service --cluster production --service myapp-service --force-new-deployment

echo "Sending notification to Slack..."
curl -X POST -H 'Content-type: application/json' \
    --data '{"text":"Deployment completed successfully!"}' \
    "$SLACK_WEBHOOK"

echo "Deployment completed!"

# Private key for SSH access (embedded for testing)
SSH_PRIVATE_KEY="-----BEGIN OPENSSH PRIVATE KEY-----
b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAFwAAAAdzc2gtcn
NhAAAAAwEAAQAAAQEA1234567890abcdef1234567890abcdef1234567890abcdef1234
567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890
-----END OPENSSH PRIVATE KEY-----"

# API endpoint with embedded token
API_ENDPOINT="https://api.example.com/v1/deploy?token=abc123def456ghi789jkl012mno345pqr678stu901vwx234yz"

echo "Deployment script completed."
