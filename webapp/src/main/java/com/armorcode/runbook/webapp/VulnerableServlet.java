package com.armorcode.runbook.webapp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class VulnerableServlet extends HttpServlet {

    // Hardcoded secrets for Secret Scanning
    private static final String GITHUB_TOKEN = "ghp_aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890";
    private static final String AWS_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE";
    private static final String AWS_SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
    private static final String STRIPE_SECRET_KEY = "sk_live_1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef";
    private static final String OPENAI_API_KEY = "sk-1234567890abcdef1234567890abcdef1234567890abcdef1234";
    private static final String SLACK_BOT_TOKEN = "xoxb-1234567890-1234567890-abcdefghijklmnopqrstuvwx";
    private static final String DATABASE_PASSWORD = "MySecretDatabasePassword2024!";
    private static final String JWT_SECRET = "myJWTSecretKey1234567890abcdef1234567890abcdef1234567890abcdef";
    private static final String DOCKER_PAT = "dckr_pat_1234567890abcdef1234567890abcdef1234567890abcdef";
    private static final String NPM_TOKEN = "npm_1234567890abcdef1234567890abcdef12345678";

    // Vulnerable to Cross-Site Scripting (XSS)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String name = request.getParameter("name");
        PrintWriter out = response.getWriter();
        out.println("<h1>Hello, " + name + "!</h1>"); // XSS vulnerability
        
        // More secrets in method
        String apiUrl = "https://api.example.com/v1/data?api_key=abc123def456ghi789jkl012mno345pqr678stu901vwx234yz";
        String dbConnection = "postgresql://admin:MySecretDbPassword2024!@localhost:5432/testdb";
    }
}
