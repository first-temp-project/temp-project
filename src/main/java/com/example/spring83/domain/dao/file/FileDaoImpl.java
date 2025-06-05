package com.example.spring83.domain.dao.file;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.spring83.domain.dto.FileDto;
import com.example.spring83.domain.mapper.FileMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FileDaoImpl implements FileDao {
	private final FileMapper fileMapper;

	@Override
	public int insert(FileDto fileDto) {
		return fileMapper.insert(fileDto);
	}

	@Override
	public List<FileDto> findByBoardNum(Long boardNum) {
		return fileMapper.findByBoardNum(boardNum);
	}

	@Override
	public int deleteByFileNum(Long fileNum) {
		return fileMapper.deleteByFileNum(fileNum);
	}
}
