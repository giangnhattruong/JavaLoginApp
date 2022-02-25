package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Account {
	private Connection con;
	private PreparedStatement stmt;
	private ResultSet rs;

	public Account(Connection con) {
		this.con = con;
	}
	
	public ArrayList<User> getUsers() throws SQLException {
		String query = "SELECT * FROM tblUser";
		stmt = con.prepareStatement(query);
		rs = stmt.executeQuery();
		
		ArrayList<User> users = new ArrayList<User>();
		
		while (rs.next()) {
			String email = rs.getString("email");
			String password = rs.getString("password");
			
			User user = new User(email, password);
			users.add(user);
		}
		
		stmt.close();
		rs.close();
		
		return users;
	}
	
	public User getUser(String email) throws SQLException {
		String query = "SELECT TOP(1) * FROM tblUser WHERE email=?";
		stmt = con.prepareStatement(query);
		stmt.setString(1, email);
		rs = stmt.executeQuery();
		
		User user = null;
		
		if (rs.next()) {
			String password = rs.getString("password");
			user = new User(email, password);
		}
		
		stmt.close();
		rs.close();
		
		return user;
	}
	
	public void addUser(String email, String password) throws SQLException {
		if (getUser(email) != null) {
			throw new SQLException("User exists.");
		}		
		
		String query = "INSERT INTO tblUser (email, password) " +
				"VALUES (?, ?)";
		stmt = con.prepareStatement(query);
		stmt.setString(1, email);
		stmt.setString(2, password);
		
		stmt.execute();
		stmt.close();
	}
	
	public void removeUser(String email, String password) throws SQLException {
		if (getUser(email) == null) {
			throw new SQLException("User doesn't exist.");
		}		
		
		String query = "DELETE tblUser " +
				"WHERE email=?";
		stmt = con.prepareStatement(query);
		stmt.setString(1, email);
		
		stmt.execute();
		stmt.close();
	}
	
	public boolean verify(String email, String password) throws SQLException {
		String query = "SELECT COUNT(*) AS count " +
				"FROM tblUser " +
				"WHERE email=? AND password=?";
		stmt = con.prepareStatement(query);
		stmt.setString(1, email);
		stmt.setString(2, password);
		
		rs = stmt.executeQuery();
		
		boolean userExists = false;
		if (rs.next())
			userExists = rs.getInt(1) == 0? false: true;
		
		stmt.close();
		rs.close();
		
		return userExists;
	}
}
