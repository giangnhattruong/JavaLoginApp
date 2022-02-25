package controller;

public class User {
	private String email;
	private String password;
	private String message;

	public User() {
	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public boolean validate() {
		if (!email.matches("\\w+@\\w+\\.\\w+")) {
			message = "Invalid email address.";
			return false;
		}

		if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,10}$")) {
			message = "Password must contain 8-10 characters, "
					+ "at least one uppercase letter, one lowercase letter, " 
					+ "one number and one special character";
			return false;
		}

		message = "Successful";
		return true;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + "]";
	}

}
