package com.armorcode.runbook;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for sending various types of notifications
 * Provides convenient methods for common notification scenarios
 */
public class NotificationUtils {
    
    private static final Logger logger = LogManager.getLogger(NotificationUtils.class);
    
    // Configuration secrets (will be detected by secret scanning)
    private static final String SECURITY_CHANNEL = "#security-alerts";
    private static final String GENERAL_CHANNEL = "#general";
    private static final String DEV_CHANNEL = "#development";
    
    // Hardcoded tokens for different environments (testing purposes)
    private static final String PROD_BOT_TOKEN = "xoxb-prod-1234567890-1234567890-abcdefghijklmnopqrstuvwx";
    private static final String DEV_BOT_TOKEN = "xoxb-dev-1234567890-1234567890-abcdefghijklmnopqrstuvwx";
    private static final String SECURITY_WEBHOOK = "https://hooks.slack.com/services/T00000000/B00000000/SECURITY123456789012345678";
    
    private final SlackNotificationService slackService;
    
    public NotificationUtils() {
        this.slackService = new SlackNotificationService();
    }
    
    public NotificationUtils(SlackNotificationService slackService) {
        this.slackService = slackService;
    }
    
    /**
     * Send a security alert to the security team
     * 
     * @param title The alert title
     * @param description The alert description
     * @param severity The severity level
     * @return true if alert was sent successfully
     */
    public boolean sendSecurityAlert(String title, String description, SecuritySeverity severity) {
        SlackNotificationService.AlertLevel alertLevel;
        
        switch (severity) {
            case LOW:
                alertLevel = SlackNotificationService.AlertLevel.INFO;
                break;
            case MEDIUM:
                alertLevel = SlackNotificationService.AlertLevel.WARNING;
                break;
            case HIGH:
                alertLevel = SlackNotificationService.AlertLevel.ERROR;
                break;
            case CRITICAL:
                alertLevel = SlackNotificationService.AlertLevel.CRITICAL;
                break;
            default:
                alertLevel = SlackNotificationService.AlertLevel.INFO;
        }
        
        logger.info("Sending security alert: {} - {}", title, severity);
        return slackService.sendAlert(SECURITY_CHANNEL, alertLevel, title, description, PROD_BOT_TOKEN);
    }
    
    /**
     * Send a deployment notification
     * 
     * @param environment The deployment environment (dev, staging, prod)
     * @param version The version being deployed
     * @param status The deployment status (started, completed, failed)
     * @return true if notification was sent successfully
     */
    public boolean sendDeploymentNotification(String environment, String version, DeploymentStatus status) {
        String emoji;
        String color;
        
        switch (status) {
            case STARTED:
                emoji = ":rocket:";
                color = "good";
                break;
            case COMPLETED:
                emoji = ":white_check_mark:";
                color = "good";
                break;
            case FAILED:
                emoji = ":x:";
                color = "danger";
                break;
            default:
                emoji = ":information_source:";
                color = "good";
        }
        
        String title = String.format("Deployment %s", status.name().toLowerCase());
        String message = String.format("%s Deployment to *%s* environment\nVersion: `%s`\nStatus: %s", 
            emoji, environment.toUpperCase(), version, status.name());
        
        String botToken = "prod".equalsIgnoreCase(environment) ? PROD_BOT_TOKEN : DEV_BOT_TOKEN;
        String channel = "prod".equalsIgnoreCase(environment) ? GENERAL_CHANNEL : DEV_CHANNEL;
        
        return slackService.sendRichMessage(channel, message, color, title, botToken);
    }
    
    /**
     * Send a vulnerability report
     * 
     * @param vulnerabilityType The type of vulnerability (dependency, code, secret)
     * @param count The number of vulnerabilities found
     * @param details Additional details about the vulnerabilities
     * @return true if report was sent successfully
     */
    public boolean sendVulnerabilityReport(VulnerabilityType vulnerabilityType, int count, String details) {
        String title = String.format("%s Vulnerabilities Detected", vulnerabilityType.getDisplayName());
        String description = String.format("Found %d %s vulnerabilities\n\nDetails:\n%s", 
            count, vulnerabilityType.getDisplayName().toLowerCase(), details);
        
        SecuritySeverity severity = count > 10 ? SecuritySeverity.HIGH : 
                                   count > 5 ? SecuritySeverity.MEDIUM : SecuritySeverity.LOW;
        
        return sendSecurityAlert(title, description, severity);
    }
    
    /**
     * Send a system health notification
     * 
     * @param serviceName The name of the service
     * @param status The health status
     * @param metrics Additional metrics or details
     * @return true if notification was sent successfully
     */
    public boolean sendHealthNotification(String serviceName, HealthStatus status, String metrics) {
        SlackNotificationService.AlertLevel alertLevel;
        String emoji;
        
        switch (status) {
            case HEALTHY:
                alertLevel = SlackNotificationService.AlertLevel.INFO;
                emoji = ":green_heart:";
                break;
            case DEGRADED:
                alertLevel = SlackNotificationService.AlertLevel.WARNING;
                emoji = ":yellow_heart:";
                break;
            case UNHEALTHY:
                alertLevel = SlackNotificationService.AlertLevel.ERROR;
                emoji = ":broken_heart:";
                break;
            case DOWN:
                alertLevel = SlackNotificationService.AlertLevel.CRITICAL;
                emoji = ":skull:";
                break;
            default:
                alertLevel = SlackNotificationService.AlertLevel.INFO;
                emoji = ":question:";
        }
        
        String title = String.format("Service Health: %s", serviceName);
        String description = String.format("%s Status: *%s*\n\nMetrics:\n%s", emoji, status.name(), metrics);
        
        return slackService.sendAlert(GENERAL_CHANNEL, alertLevel, title, description, PROD_BOT_TOKEN);
    }
    
    /**
     * Send a custom notification with webhook
     * 
     * @param message The message to send
     * @param webhookUrl The webhook URL (optional, uses default if null)
     * @return true if notification was sent successfully
     */
    public boolean sendCustomNotification(String message, String webhookUrl) {
        if (webhookUrl != null && !webhookUrl.isEmpty()) {
            return slackService.sendMessageWithWebhook(message, webhookUrl);
        } else {
            return slackService.sendMessageWithWebhook(message, SECURITY_WEBHOOK);
        }
    }
    
    /**
     * Close resources
     */
    public void close() {
        if (slackService != null) {
            slackService.close();
        }
    }
    
    // Enums for different notification types
    
    public enum SecuritySeverity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    public enum DeploymentStatus {
        STARTED, COMPLETED, FAILED
    }
    
    public enum VulnerabilityType {
        DEPENDENCY("Dependency"),
        CODE("Code Quality"),
        SECRET("Secret Scanning"),
        INFRASTRUCTURE("Infrastructure");
        
        private final String displayName;
        
        VulnerabilityType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum HealthStatus {
        HEALTHY, DEGRADED, UNHEALTHY, DOWN
    }
    
    /**
     * Example usage
     */
    public static void main(String[] args) {
        NotificationUtils notificationUtils = new NotificationUtils();
        
        try {
            // Example notifications
            notificationUtils.sendSecurityAlert(
                "High Severity Vulnerability", 
                "Critical security vulnerability detected in authentication module", 
                SecuritySeverity.HIGH
            );
            
            notificationUtils.sendDeploymentNotification("prod", "v1.2.3", DeploymentStatus.COMPLETED);
            
            notificationUtils.sendVulnerabilityReport(
                VulnerabilityType.DEPENDENCY, 
                15, 
                "Multiple outdated dependencies with known CVEs detected"
            );
            
            notificationUtils.sendHealthNotification(
                "Authentication Service", 
                HealthStatus.DEGRADED, 
                "Response time: 2.5s (threshold: 2.0s)\nError rate: 3.2%"
            );
            
        } finally {
            notificationUtils.close();
        }
    }
}
