package com.blogsphere.exception;

public class AiApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AiApiException(String message) {
        super(message);
    }

    public AiApiException(String message, Throwable cause) {
        super(message, cause);
    }
}