package com.nickstephen.lib.http;

public class NetException extends Exception {
	public static final int NON_HTTP_RESPONSE_CODE = -1;
	public static final int NULL_RESPONSE_CODE = -2;
	public static final int JSON_ERROR_CODE = -3;
	
	private final int statusCode;

	/**
	 * wtf
	 */
	private static final long serialVersionUID = 8022232184943781832L;

	public NetException(int errCode) {
		statusCode = errCode;
	}

	public NetException(String detailMessage, int errCode) {
		super(detailMessage);
		
		statusCode = errCode;
	}

	public NetException(Throwable throwable, int errCode) {
		super(throwable);
		
		statusCode = errCode;
	}

	public NetException(String detailMessage, Throwable throwable, int errCode) {
		super(detailMessage, throwable);
		
		statusCode = errCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
