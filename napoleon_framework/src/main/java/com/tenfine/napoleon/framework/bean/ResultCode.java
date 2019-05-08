package com.tenfine.napoleon.framework.bean;

public enum ResultCode {
	SUCCESS(1, "success"), // 成功
	ERROR(0, "error"), // 错误
	NEED_TO_LOGIN(10, "need to login"), // 需要登陆
	ILLEGAL_ARGUMENT(2, "illegal argument");// 非法参数

	private final int code;// 代号
	private final String depict;// 描述

	ResultCode(int code, String depict) {
		this.code = code;
		this.depict = depict;
	}

	public int getCode() {
		return code;
	}

	public String getDepict() {
		return depict;
	}

}

