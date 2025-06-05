package com.example.spring83.domain.dao.file;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.spring83.domain.dto.FileDto;

@Repository
public interface FileDao {
	public abstract int insert(FileDto fileDto);

	public abstract List<FileDto> findByBoardNum(Long boardNum);

	public abstract int deleteByFileNum(Long fileNum);
	
}
