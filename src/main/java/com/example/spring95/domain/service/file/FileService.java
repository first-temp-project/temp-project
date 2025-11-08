package com.example.spring95.domain.service.file;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.spring95.domain.dto.BoardDto;
import com.example.spring95.domain.dto.FileDto;

public interface FileService {
	public abstract void insertFiles(List<FileDto> insertFiles, Long boardNum);

	public abstract List<FileDto> findByBoardNum(Long boardNum);

	public abstract void deleteFiles(List<FileDto> deleteFiles);

	public abstract int countByBoardNum(Long boardNum);

	public abstract List<FileDto> findByYesterDay();

	public abstract boolean isImage(File file);

	public abstract String getContentType(File file);

	public abstract File getUploadPath(String parent, String child);

	public abstract void createThumbnails(MultipartFile multipartFile, File thumbnailFile, int size);

	public abstract FileDto upload(MultipartFile multipartFile, String directoryPath, String datePath);

	public abstract String getFilePath(FileDto fileDto);

	public abstract String getFileThumbnailPath(FileDto fileDto);

	public abstract void setFilePath(FileDto fileDto);

	public abstract void autoDeleteFiles(List<FileDto> yesterdayFiles, String directoryPath, String yesterdayPath);

}
