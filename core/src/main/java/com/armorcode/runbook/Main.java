package com.armorcode.runbook;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
  private static final String AWS_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE";
  private static final String AWS_SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";

  public static void main(String[] args) {
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    System.out.printf("Hello and welcome!");

    for (int i = 1; i <= 5; i++) {
      //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons
        // .Debugger.Db_set_breakpoint"/> breakpoint
      // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
      System.out.println("i = " + i);
    }
  }

  // Vulnerable to SQL Injection
  public java.sql.ResultSet vulnerableSQL(String username, java.sql.Connection connection) throws java.sql.SQLException {
    String sql = "SELECT * FROM users WHERE username = '" + username + "'";
    java.sql.Statement statement = connection.createStatement();
    return statement.executeQuery(sql);
  }

  // Vulnerable to Command Injection
  public void vulnerableCommand(String command) throws java.io.IOException {
      Runtime.getRuntime().exec(command);
  }
}
