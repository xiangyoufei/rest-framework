package com.example.demo.web.controller;

import org.apache.shiro.ShiroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.core.dto.Response;
import com.example.demo.core.dto.ResponseBean;
import com.example.demo.security.TokenException;

@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());  

	/**捕捉shiro异常*/
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(ShiroException.class)
	public ResponseBean handle401(ShiroException e) {
		logger.info(" ========================= handle401 ===========================");
		return new ResponseBean(401, e.getMessage(), null);
	}
	
	/**捕捉token异常*/
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(TokenException.class)
	public ResponseBean handleTokenException(TokenException e) {
		return new ResponseBean(401, e.getMessage(), null);
	}


	/**
	 * 400 - Bad Request
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Response handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		logger.error("参数解析失败", e);
		return new Response().failure("could_not_read_json");
	}

	/**
	 * 405 - Method Not Allowed
	 */
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Response handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		logger.error("不支持当前请求方法", e);
		return new Response().failure("request_method_not_supported");
	}

	/**
	 * 415 - Unsupported Media Type
	 */
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public Response handleHttpMediaTypeNotSupportedException(Exception e) {
		logger.error("不支持当前媒体类型", e);
		return new Response().failure("content_type_not_supported");
	}

	/**
	 * 500 - Internal Server Error
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public Response handleException(Exception e) {
		logger.error("服务运行异常", e);
		return new Response().failure(e.getMessage());
	}
}
