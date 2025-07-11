package com.armorcode.runbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for sending notifications to Slack channels
 * Supports both Bot Token and Webhook URL methods
 */
public class SlackNotificationService {
    
    private static final Logger logger = LogManager.getLogger(SlackNotificationService.class);
    
    // Slack API endpoints
    private static final String SLACK_API_BASE_URL = "https://slack.com/api";
    private static final String POST_MESSAGE_ENDPOINT = "/chat.postMessage";
    
    // Hardcoded secrets for testing (these will be detected by secret scanning)
    private static final String DEFAULT_BOT_TOKEN = "xoxb-1234567890-1234567890-abcdefghijklmnopqrstuvwx";
    private static final String DEFAULT_WEBHOOK_URL = "https://hooks.slack.com/services/T00000000/B00000000/XXXXXXXXXXXXXXXXXXXXXXXX";
    
    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;
    
    public SlackNotificationService() {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClients.createDefault();
    }
    
    /**
     * Send a message to a Slack channel using Bot Token
     * 
     * @param channelName The channel name (e.g., "#general" or "general")
     * @param message The message to send
     * @param botToken The Slack Bot Token (xoxb-...)
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendMessageWithBotToken(String channelName, String message, String botToken) {
        try {
            // Ensure channel name starts with #
            String formattedChannel = channelName.startsWith("#") ? channelName : "#" + channelName;
            
            // Prepare the request payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("channel", formattedChannel);
            payload.put("text", message);
            payload.put("as_user", true);
            
            String jsonPayload = objectMapper.writeValueAsString(payload);
            
            // Create HTTP POST request
            HttpPost httpPost = new HttpPost(SLACK_API_BASE_URL + POST_MESSAGE_ENDPOINT);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + botToken);
            httpPost.setEntity(new StringEntity(jsonPayload));
            
            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            
            int statusCode = response.getStatusLine().getStatusCode();
            
            if (statusCode == 200) {
                logger.info("Successfully sent message to Slack channel: {}", formattedChannel);
                logger.debug("Slack API response: {}", responseBody);
                return true;
            } else {
                logger.error("Failed to send message to Slack. Status: {}, Response: {}", statusCode, responseBody);
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Error sending message to Slack channel: {}", channelName, e);
            return false;
        }
    }
    
    /**
     * Send a message to a Slack channel using Webhook URL
     * 
     * @param message The message to send
     * @param webhookUrl The Slack Webhook URL
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendMessageWithWebhook(String message, String webhookUrl) {
        try {
            // Prepare the request payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("text", message);
            
            String jsonPayload = objectMapper.writeValueAsString(payload);
            
            // Create HTTP POST request
            HttpPost httpPost = new HttpPost(webhookUrl);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonPayload));
            
            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            
            if (statusCode == 200) {
                logger.info("Successfully sent message via Slack webhook");
                return true;
            } else {
                String responseBody = EntityUtils.toString(response.getEntity());
                logger.error("Failed to send message via webhook. Status: {}, Response: {}", statusCode, responseBody);
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Error sending message via Slack webhook", e);
            return false;
        }
    }
    
    /**
     * Send a message using default bot token (for testing purposes)
     * 
     * @param channelName The channel name
     * @param message The message to send
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendMessage(String channelName, String message) {
        return sendMessageWithBotToken(channelName, message, DEFAULT_BOT_TOKEN);
    }
    
    /**
     * Send a message using default webhook URL (for testing purposes)
     * 
     * @param message The message to send
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendMessageWithDefaultWebhook(String message) {
        return sendMessageWithWebhook(message, DEFAULT_WEBHOOK_URL);
    }
    
    /**
     * Send a rich message with attachments to Slack
     * 
     * @param channelName The channel name
     * @param message The main message text
     * @param color The color of the attachment (good, warning, danger, or hex color)
     * @param title The title of the attachment
     * @param botToken The Slack Bot Token
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendRichMessage(String channelName, String message, String color, String title, String botToken) {
        try {
            String formattedChannel = channelName.startsWith("#") ? channelName : "#" + channelName;
            
            // Create attachment
            Map<String, Object> attachment = new HashMap<>();
            attachment.put("color", color);
            attachment.put("title", title);
            attachment.put("text", message);
            attachment.put("ts", System.currentTimeMillis() / 1000);
            
            // Prepare the request payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("channel", formattedChannel);
            payload.put("attachments", new Object[]{attachment});
            
            String jsonPayload = objectMapper.writeValueAsString(payload);
            
            // Create HTTP POST request
            HttpPost httpPost = new HttpPost(SLACK_API_BASE_URL + POST_MESSAGE_ENDPOINT);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + botToken);
            httpPost.setEntity(new StringEntity(jsonPayload));
            
            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            
            if (statusCode == 200) {
                logger.info("Successfully sent rich message to Slack channel: {}", formattedChannel);
                return true;
            } else {
                String responseBody = EntityUtils.toString(response.getEntity());
                logger.error("Failed to send rich message to Slack. Status: {}, Response: {}", statusCode, responseBody);
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Error sending rich message to Slack channel: {}", channelName, e);
            return false;
        }
    }
    
    /**
     * Send an alert message with predefined formatting
     * 
     * @param channelName The channel name
     * @param alertLevel The alert level (INFO, WARNING, ERROR, CRITICAL)
     * @param title The alert title
     * @param description The alert description
     * @param botToken The Slack Bot Token
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendAlert(String channelName, AlertLevel alertLevel, String title, String description, String botToken) {
        String color;
        String emoji;
        
        switch (alertLevel) {
            case INFO:
                color = "good";
                emoji = ":information_source:";
                break;
            case WARNING:
                color = "warning";
                emoji = ":warning:";
                break;
            case ERROR:
                color = "danger";
                emoji = ":x:";
                break;
            case CRITICAL:
                color = "#FF0000";
                emoji = ":rotating_light:";
                break;
            default:
                color = "good";
                emoji = ":bell:";
        }
        
        String message = String.format("%s *%s*\n%s", emoji, title, description);
        return sendRichMessage(channelName, message, color, alertLevel.name() + " Alert", botToken);
    }
    
    /**
     * Close the HTTP client resources
     */
    public void close() {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            logger.error("Error closing HTTP client", e);
        }
    }
    
    /**
     * Alert levels for structured notifications
     */
    public enum AlertLevel {
        INFO, WARNING, ERROR, CRITICAL
    }
    
    /**
     * Example usage and testing method
     */
    public static void main(String[] args) {
        SlackNotificationService slackService = new SlackNotificationService();
        
        try {
            // Test with hardcoded secrets (will be detected by secret scanning)
            String testBotToken = "xoxb-test-1234567890-1234567890-abcdefghijklmnopqrstuvwx";
            String testWebhook = "https://hooks.slack.com/services/T1234567890/B1234567890/abcdefghijklmnopqrstuvwx";
            
            // Example usage
            boolean success1 = slackService.sendMessage("#general", "Hello from GHAS Test Project!");
            boolean success2 = slackService.sendMessageWithWebhook("Test webhook message", testWebhook);
            boolean success3 = slackService.sendAlert("#alerts", AlertLevel.WARNING, 
                "Security Alert", "Potential vulnerability detected in dependencies", testBotToken);
            
            System.out.println("Message 1 sent: " + success1);
            System.out.println("Message 2 sent: " + success2);
            System.out.println("Alert sent: " + success3);
            
        } finally {
            slackService.close();
        }
    }
}
