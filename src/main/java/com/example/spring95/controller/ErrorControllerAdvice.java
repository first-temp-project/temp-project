package com.example.spring95.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/*@ControllerAdvice*/
public class ErrorControllerAdvice {

	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e, HttpServletRequest req, HttpServletResponse resp) {
		e.printStackTrace();
		System.out.println("controllerAdvice");
//		Integer statusCode = (Integer) req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//		statusCode = statusCode == null ? HttpStatus.INTERNAL_SERVER_ERROR.value() : statusCode;
		resp.setStatus(400);
		return "error/error";

	}

}
