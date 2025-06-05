package com.example.spring83.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.spring83.domain.dto.FileDto;

@Mapper
public interface FileMapper {
	public abstract int insert(FileDto fileDto);

	public abstract List<FileDto> findByBoardNum(Long boardNum);

	public abstract int deleteByFileNum(Long fileNum);
}
