package com.tcs.authService.dto;

public class LoginResponse {
	private String userId;
	private String username;
	private String message;
	private String token; // 🟢 Added

	// Constructor for Login (with token)
	public LoginResponse(String userId, String username, String message, String token) {
		this.userId = userId;
		this.username = username;
		this.message = message;
		this.token = token;
	}

	// Constructor for Register (no token)
	public LoginResponse(String userId, String username, String message) {
		this.userId = userId;
		this.username = username;
		this.message = message;
		this.token = null;
	}

	public String getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public String getMessage() {
		return message;
	}

	public String getToken() {
		return token;
	}
}