package com.example.spring83.service;

import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.spring95.common.DateUtil;
import com.example.spring95.domain.service.file.FileService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/*.xml")
public class ServiceTest {
	private static final String DEFAULT_DIRECTORY = "C:/upload/files";
	@Autowired
	private FileService fileService;



	@Test
	public void serviceTest() {
		
		Integer n = 3;
		System.out.println(Objects.equals(3L, 3L));
		System.out.println(Objects.equals(3L, null));
		System.out.println(Objects.equals(3L, n));
		System.out.println(Objects.equals(3L, "3L"));
		System.out.println(Objects.equals(null, null));
	}

}
