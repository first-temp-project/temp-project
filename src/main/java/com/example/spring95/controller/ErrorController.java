package com.example.spring95.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;

@Controller
@RequestMapping("/error")
public class ErrorController {
	private static final String DEFAULT_ERROR_VIEW = "error/error";

	@RequestMapping("/{errorCode}")
	public String errorHandler(@PathVariable int errorCode, HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("errorController");
//		Throwable ex = (Throwable) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
//		if (ex != null) {
//			ex.printStackTrace();
//		}
		resp.setStatus(errorCode);
		return DEFAULT_ERROR_VIEW;
		
	}
}
