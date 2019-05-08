package com.tenfine.napoleon.framework.exception;

public class FrameworkRuntimeException extends RuntimeException {
 
	private static final long serialVersionUID = 1819331933552900119L;

	public FrameworkRuntimeException(String msg) {
        super(msg);
    }

    public FrameworkRuntimeException(String msg, Throwable ex) {
        super(msg, ex);
    }
}

