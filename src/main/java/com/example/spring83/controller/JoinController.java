package com.example.spring83.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring83.common.MessageConstants;
import com.example.spring83.common.SessionUtil;
import com.example.spring83.domain.dto.UserDto;
import com.example.spring83.domain.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/join")
@Controller
@RequiredArgsConstructor
public class JoinController {
	private final UserService userService;
	private final HttpSession session;

	@GetMapping()
	public String join() {
		if (session.getAttribute(SessionUtil.KEY) != null) {
			session.invalidate();
		}
		return "join/join";
	}

	@PostMapping()
	public String join(UserDto userDto, RedirectAttributes attributes) {
		if (!userService.insert(userDto)) {
			attributes.addFlashAttribute("user", userDto);
			attributes.addFlashAttribute("msg", MessageConstants.ERROR_MSG);
			return "redirect:/join/join";
		}
		return "redirect:/login";
	}

	@PostMapping(value = "/checkId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkId(@RequestBody UserDto userDto) {
		Boolean result = userService.findByUserId(userDto.getUserId()) == null;
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@PostMapping(value = "/checkEmail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkEmail(@RequestBody UserDto userDto) {
		Boolean result = userService.findByUserEmail(userDto.getUserEmail()) == null;
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
}
