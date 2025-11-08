package com.example.spring95.domain.service.admin;

import org.springframework.stereotype.Service;

import com.example.spring95.common.Dbconstants;
import com.example.spring95.domain.dao.admin.AdminDao;
import com.example.spring95.domain.dto.CommentDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImple implements AdminService {
	private final AdminDao adminDao;

	@Override
	public boolean deleteComment(Long commentNum) {
		try {
			return adminDao.deleteComment(commentNum) == Dbconstants.SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("adminService deleteComment error");
			return false;
		}
	}

	@Override
	public boolean updateComment(CommentDto commentDto) {
		try {
			return adminDao.updateComment(commentDto) == Dbconstants.SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("adminService updateComment error");
			return false;
		}
	}

	@Override
	public boolean deleteBoard(Long boardNum) {
		try {
			return adminDao.deleteBoard(boardNum) == Dbconstants.SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("adminService updateComment error");
			return false;
		}
	}

}
