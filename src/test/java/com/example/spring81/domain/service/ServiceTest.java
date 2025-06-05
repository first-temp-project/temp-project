package com.example.spring81.domain.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.spring83.domain.dto.Criteria;
import com.example.spring83.domain.service.board.BoardService;
import com.example.spring83.domain.service.user.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/*.xml")
@WebAppConfiguration
public class ServiceTest {
	@Autowired
	private UserService userService;

	@Test
	public void test() {
		System.out.println("123".equals(""));
		System.out.println("123".equals(null));
	}
}
