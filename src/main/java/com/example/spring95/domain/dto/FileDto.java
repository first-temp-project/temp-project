package com.example.spring95.domain.dto;

import lombok.Data;

@Data
public class FileDto {
	private Long fileNum;
	private String fileUuid;
	private String fileUploadPath;
	private boolean fileType;
	private String fileName;
	private Long fileSize;
	private Long boardNum;
	private String filePath;
}
