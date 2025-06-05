package com.example.spring83.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.spring83.domain.dto.Criteria;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@GetMapping("/test1")
	public String test1(Criteria criteria, Model model) {
		return "test/test1";
	}
	
	@PostMapping("/test2")
	public String test2(Criteria criteria, String name) {
		System.out.println("test2=="+criteria);
		return "test/test3";
	}
	
	@GetMapping("/test3")
	public String test3(Criteria criteria) {
		System.out.println("test3=="+criteria);
		return "test/test3";
	}
}
