package com.tenfine.napoleon.framework.exception;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/****
 * 1.exception 的分类和处理方法
 * 定义两类异常:
 * Checked Exception: Exception的子类,方法签名上需要显示的声明throws，编译器迫使调用者处理这类异常或者声明throws继续往上抛。
 * Unchecked Exception: RuntimeException的子类，方法签名不需要声明throws，编译器也不会强制调用者处理该类异常。
 * 我们要谈的是 CheckedException：
 * 应用级的exception： 由应用在运行过程中抛出的exception，主要跟应用的业务逻辑有关, 并且这些exception实在应用中定义的， 比如：输入的登陆账号找不到，登录失败。
 * 系统级的exception：应用在运行过程中系统抛出的异常，如IO错误，运算错误等等。
 * 
 * 2.集中的Exception handler 如何定义及使用
 * System Exception: Exception 和 SQL exception
 * Business Exception: BadRequestException, NotFoundException,ServerRejectException, SystemException
 * 
 * 3.HTTP request的 exception返回设计
 * 在每个应用Exception 中设计不同的返回消息和errorcode：并且返回的status 也不同。
 * 200 – OK
 * 400 - Bad Request
 * 403 – Forbidden
 * 404 – Not Found
 * 503 – Internal servererror
 * 5.异常处理的原则和技巧
 * 参考：http://lavasoft.blog.51cto.com/62575/18920/
 * 1、避免过大的try块，不要把不会出现异常的代码放到try块里面，尽量保持一个try块对应一个或多个异常。
 * 2、细化异常的类型，不要不管什么类型的异常都写成Excetpion。
 * 3、catch块尽量保持一个块捕获一类异常，不要忽略捕获的异常，捕获到后要么处理，要么转译，要么重新抛出新类型的异常。
 * 4、不要把自己能处理的异常抛给别人。
 * 5、不要用try...catch参与控制程序流程，异常控制的根本目的是处理程序的非正常情况
 * 6.优点
 * a)Business方面的信息反馈给client,方便系统集成和调试
 * b)系统级的Exception有利于 function测试 发现隐藏的bug
 * 
 */

@ControllerAdvice
public class GlobalExceptionController {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);

	@ExceptionHandler(SQLException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ExceptionModel handleSQLException(HttpServletRequest request, SQLException ex) {
		handleLog(request, ex);
		ExceptionModel em = new ExceptionModel(StatusCode.INTERNAL_SERVER_ERROR,0,ex.getMessage());
		return em;
	}

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ExceptionModel handleBadRequestException(HttpServletRequest request, BadRequestException ex) {
		handleLog(request, ex);
		ExceptionModel em = new ExceptionModel(StatusCode.BAD_REQUEST,StatusCode.BAD_REQUEST.value(),ex.getMessage());
		return em;
	}

	@ExceptionHandler(ServerRejectException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ResponseBody
	public ExceptionModel handleServerRejectException(HttpServletRequest request, ServerRejectException ex) {
		handleLog(request, ex);
		ExceptionModel em = new ExceptionModel(StatusCode.FORBIDDEN,StatusCode.BAD_REQUEST.value(),ex.getMessage());
		return em;
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public ExceptionModel handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
		handleLog(request, ex);
		ExceptionModel em = new ExceptionModel(StatusCode.NOT_FOUND,StatusCode.BAD_REQUEST.value(),ex.getMessage());
		return em;
	}

	@ExceptionHandler(SystemException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ExceptionModel handleSystemException(HttpServletRequest request, SystemException ex) {
		handleLog(request, ex);
		ExceptionModel em = new ExceptionModel(StatusCode.INTERNAL_SERVER_ERROR,StatusCode.BAD_REQUEST.value(),ex.getMessage());
		return em;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ExceptionModel handleAllException(HttpServletRequest request, Exception ex) {
		if(!(ex instanceof SystemException)
				&& !(ex instanceof NotFoundException)
				&& !(ex instanceof SQLException)
				&& !(ex instanceof BadRequestException)
				&& !(ex instanceof ServerRejectException)
				){
			handleLog(request, ex);
			ExceptionModel em = new ExceptionModel(StatusCode.INTERNAL_SERVER_ERROR,StatusCode.INTERNAL_SERVER_ERROR.value(),ex.getMessage());
			return em;
		}else{
			return null;
		}
	}

	private void handleLog(HttpServletRequest request, Exception ex) {
		StringBuffer logBuffer = new StringBuffer();
		if (request != null) {
			logBuffer.append("  request method=" + request.getMethod());
			logBuffer.append("  url=" + request.getRequestURL());
		}
		if (ex instanceof CommonException) {
			logBuffer.append("  msg=" + ((CommonException) ex).getMessage());
		}
		if (ex != null) {
			logBuffer.append("  exception:" + ex);
		}
		logger.error(logBuffer.toString());
	}
}
