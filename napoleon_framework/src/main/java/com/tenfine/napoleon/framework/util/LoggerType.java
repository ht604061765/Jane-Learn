package com.tenfine.napoleon.framework.util;

/**
 * 配置分类枚举
 */
public enum LoggerType{
	
	INFO 	(1, "INFO", "信息"),
	WARN 	(2, "WARN", "警告"),
	ERROR 	(3, "ERROR", "错误"), 
	TRACE 	(4, "TRACE", "调试");
	
	private int code ;
	private String name ;
	private String memo ;
	
	private LoggerType(int code , String name , String memo){
		this.code = code;
		this.name = name;
		this.memo = memo;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}