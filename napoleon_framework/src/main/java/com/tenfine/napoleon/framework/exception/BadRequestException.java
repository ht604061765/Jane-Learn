package com.tenfine.napoleon.framework.exception;

/***
 * 参数异常
 */
public class BadRequestException extends CommonException {

	public BadRequestException(String msg,Throwable e) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;

}
  
