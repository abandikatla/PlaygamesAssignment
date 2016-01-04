package com.playgames.dto.response;

public class ErrorResponse extends Response {

	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
