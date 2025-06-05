package com.example.spring83.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.spring83.domain.dto.Criteria;
import com.example.spring83.domain.dto.ItemDto;
import com.example.spring83.domain.dto.PageDto;
import com.example.spring83.domain.service.item.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
	private final ItemService itemService;
	private static final int AMOUNT = 30;

	@GetMapping("/list")
	public String list(Criteria criteria, Model model) {
		criteria.setAmount(AMOUNT);
		model.addAttribute("items", itemService.findByCriteria(criteria));
		model.addAttribute("pageDto", new PageDto(criteria, itemService.countByCriteria(criteria)));
		return "item/list";
	}

	@GetMapping("/register")
	public String register() {
		return "item/register";
	}

	@PostMapping("/register")
	public String register(Criteria criteria, ItemDto itemDto) {
		return "redirect:/item/list";
	}

	@GetMapping("/update")
	public String update() {
		return "item/update";
	}
}
