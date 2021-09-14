package com.main.videosapi.responses;

public class ResponseHandler {
	private boolean success;
	private String message;
	private Object data;

	public ResponseHandler(boolean success, String message, Object object) {
		super();
		this.success = success;
		this.message = message;
		this.data = object;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
