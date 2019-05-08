package com.tenfine.napoleon.framework.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 控制层返回数据的统一响应规范（Rest形式下）
 */
// @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Result<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private int status; // 状态
	private String message; // 信息
	private T data; // 调用成功时，返回业务数据

	private Result(int status) {
		this.status = status;
	}

	private Result(int status, T data) {
		this.status = status;
		this.data = data;
	}

	private Result(int status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	private Result(int status, String message) {
		this.status = status;
		this.message = message;
	}

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    @JsonIgnore
	public boolean isSuccess() {
		return this.status == ResultCode.SUCCESS.getCode();
	}

	public int getStatus() {
		return this.status;
	}

	public String getMessage() {
		return this.message;
	}

	public T getData() {
		return this.data;
	}

	public static <T> Result<T> success() {
		return new Result<T>(ResultCode.SUCCESS.getCode());
	}

	public static <T> Result<T> success(String message) {
		return new Result<T>(ResultCode.SUCCESS.getCode(), message);
	}

	public static <T> Result<T> success(T data) {
		return new Result<T>(ResultCode.SUCCESS.getCode(), data);
	}

	public static <T> Result<T> success(String message, T data) {
		return new Result<T>(ResultCode.SUCCESS.getCode(), message, data);
	}

	public static <T> Result<T> error() {
		return new Result<T>(ResultCode.ERROR.getCode(), ResultCode.ERROR.getDepict());
	}

	public static <T> Result<T> error(String message) {
		return new Result<T>(ResultCode.ERROR.getCode(), message);
	}

	public static <T> Result<T> error(int status, String message) {
		return new Result<T>(status, message);
	}
}

