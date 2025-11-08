package com.example.spring95.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring95.common.MessageConstants;
import com.example.spring95.common.SessionUtil;
import com.example.spring95.common.ViewPathUtil;
import com.example.spring95.domain.dto.UserDto;
import com.example.spring95.domain.service.user.UserService;
import com.mysql.cj.Session;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final HttpSession session;
	private final UserService userService;
	private static final String COOKIE_KEY = "id";
	private static final String BASE_PATH = "login";

	@GetMapping("/login")
	public String login() {
		if (SessionUtil.isLogin(session)) {
			return ViewPathUtil.REDIRECT_MAIN;
		}
		return ViewPathUtil.getForwardPath(BASE_PATH, BASE_PATH);
	}

	@PostMapping("/login")
	public String login(String userId, String userPassword, boolean rememberId, HttpServletResponse resp,
			RedirectAttributes attributes) {
		UserDto userDto = userService.login(userId, userPassword);
		if (userDto == null) {
			MessageConstants.addErrorMessage(attributes, MessageConstants.LOGIN_ERROR_MSG);
			return ViewPathUtil.REDIRECT_LOGIN;
		}
		setCookie(userId, rememberId, resp);
		SessionUtil.addIsAdminToSession(session, userDto);
		SessionUtil.addUserNumToSession(session, userDto);
		return ViewPathUtil.REDIRECT_MAIN;
	}

	@GetMapping("/logout")
	public String logout() {
		if (SessionUtil.isLogin(session)) {
			session.invalidate();
		}
		return ViewPathUtil.REDIRECT_LOGIN;
	}

	private void setCookie(String userId, boolean rememberId, HttpServletResponse resp) {
		Cookie cookie = new Cookie(COOKIE_KEY, rememberId ? userId : "");
		cookie.setPath("/");
		cookie.setMaxAge(rememberId ? 60 * 60 * 24 * 15 : 0);
		resp.addCookie(cookie);
	}

}
