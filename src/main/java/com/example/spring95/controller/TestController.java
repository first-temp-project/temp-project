package com.example.spring95.controller;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring95.domain.dto.Criteria;

@Controller
@RequestMapping("/test")
public class TestController {

	@GetMapping("/test1")
	public String test1(RedirectAttributes attributes) {
		System.out.println("들어옴");
		attributes.addAttribute("msg", "hello world");
		return "forward:/test/test2?msg=hello";
	}

	@GetMapping("/test2")
	public String test2(boolean result) {
		return "test/test2";
	}

	@PostMapping("/test2")
	public String test3() {
		int a = 5 / 0;
		return "test/test3";
	}

	@GetMapping("/test4")
	public String test4() {
		return "test/test4";
	}

}
