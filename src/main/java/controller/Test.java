package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Test {

	public static void main(String[] args) {
		try {
			// Loading MSSQL-JDBC driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Create a variable for the connection string.
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=RTTO;integratedSecurity=true;trustServerCertificate=true";
        try (Connection con = DriverManager.getConnection(connectionUrl);) {
        	String query = "SELECT email FROM tblUser";
        	String email = "007@agent.cia";
        	String password = "James@007";

    		Account account = new Account(con);
    		
    		var users = account.getUsers();
    		for (User user: users)
    			System.out.println(user);
    		
    		System.out.println("Is Existed: " + account.verify(email, password));
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
	}

}
