package com.example.spring95.domain.dto;

import lombok.Data;

@Data
public class UserDto {
	private Long userNum;
	private String userId;
	private String userPassword;
	private String userPasswordCheck;
	private String userEmail;
	private String userZipCode;
	private String userAddress;
	private String userDetailAddress;
	private String userPhone;
	private boolean userRole;
}
