package com.armorcode.runbook.webapp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class VulnerableServlet extends HttpServlet {

    // Hardcoded secret for Secret Scanning
    private static final String GITHUB_TOKEN = "ghp_aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890";

    // Vulnerable to Cross-Site Scripting (XSS)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String name = request.getParameter("name");
        PrintWriter out = response.getWriter();
        out.println("<h1>Hello, " + name + "!</h1>"); // XSS vulnerability
    }
}
