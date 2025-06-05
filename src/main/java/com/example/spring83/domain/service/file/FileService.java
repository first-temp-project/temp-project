package com.example.spring83.domain.service.file;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.spring83.domain.dto.BoardDto;
import com.example.spring83.domain.dto.FileDto;

public interface FileService {
	public abstract List<FileDto> findByBoardNum(Long boardNum);
	public void insertFiles(BoardDto boardDto);
	public String getDatePath();
	public File getUploadPath(String parent, String child);
	public boolean isImage(File file);
	public FileDto upload(MultipartFile multipartFile, File uploadPath, String datePath);
	public void createThumbnail(File originalFile, File thumbnailFile, int thumbnailSize);
	public void deleteFiles(BoardDto boardDto);
	public String getContentType(File file);
}
