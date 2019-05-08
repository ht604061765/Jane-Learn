package com.tenfine.napoleon.framework.exception;


public class ExceptionModel {
	private StatusCode status;
	private Integer errorCode;
	private String message;
	
	public ExceptionModel(StatusCode status,Integer errorCode,String message){
		this.status = status;
		this.errorCode = errorCode;
		this.message = message;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
