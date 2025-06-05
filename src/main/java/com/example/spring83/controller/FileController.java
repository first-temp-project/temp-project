package com.example.spring83.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring83.domain.dto.FileDto;
import com.example.spring83.domain.service.file.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
	private final FileService fileService;
	private static final String DEFAULT_DERECTORY = "C:/upload/practice";

	@GetMapping("/display")
	public ResponseEntity<byte[]> display(String fileName) {
		File file = new File(DEFAULT_DERECTORY, fileName);
		if (!file.exists()) {
			return ResponseEntity.notFound().build();
		}
		try {
			String contentType = fileService.getContentType(file);
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", contentType);
			byte[] result = FileCopyUtils.copyToByteArray(file);
			return new ResponseEntity<byte[]>(result, header, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}

	}

	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> download(String fileName) {
		File file = new File(DEFAULT_DERECTORY, fileName);
		Resource resource = new FileSystemResource(file);
		if (!file.exists() || !resource.exists()) {
			return ResponseEntity.notFound().build();
		}
		String resourceName = resource.getFilename();
		resourceName = resourceName.substring(resourceName.indexOf("_") + 1);
		HttpHeaders header = new HttpHeaders();
		try {
			header.add("Content-Disposition",
					"attachment;filename=" + new String(resourceName.getBytes("UTF-8"), "ISO-8859-1"));
			return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping(value = "/{boardNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FileDto>> getFiles(@PathVariable Long boardNum) {
		return new ResponseEntity<List<FileDto>>(fileService.findByBoardNum(boardNum), HttpStatus.OK);
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FileDto>> upload(MultipartFile[] multipartFiles) {
		if (multipartFiles == null) {
			return ResponseEntity.badRequest().build();
		}
		List<FileDto> files = new ArrayList<>();
		String datePath = fileService.getDatePath();
		File uploadPath = fileService.getUploadPath(DEFAULT_DERECTORY, datePath);
		for (MultipartFile multipartFile : multipartFiles) {
			FileDto fileDto;
			try {
				fileDto = fileService.upload(multipartFile, uploadPath, datePath);
				files.add(fileDto);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().build();
			}
		}
		return new ResponseEntity<List<FileDto>>(files, HttpStatus.OK);
	}

	@GetMapping(value = "/length/{boardNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public int getFileLength(@PathVariable Long boardNum) {
		System.out.println(fileService.findByBoardNum(boardNum).size());
		return fileService.findByBoardNum(boardNum).size();
	}
}
