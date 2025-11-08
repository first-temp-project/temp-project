package com.example.spring95.domain.service.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring95.common.DateUtil;
import com.example.spring95.common.Dbconstants;
import com.example.spring95.domain.dao.file.FileDao;
import com.example.spring95.domain.dto.BoardDto;
import com.example.spring95.domain.dto.FileDto;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
	private static final int THUMBNAIL_SIZE = 100;
	private final FileDao fileDao;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void insertFiles(List<FileDto> insertFiles, Long boardNum) {
		if (insertFiles == null || insertFiles.isEmpty()) {
			return;
		}

		for (FileDto insertFile : insertFiles) {
			insertFile.setBoardNum(boardNum);
			boolean isSuccess = fileDao.insert(insertFile) == Dbconstants.SUCCESS_CODE;
			if (!isSuccess) {
				throw new RuntimeException("fileService insertFiles error");
			}
		}
	}

	@Override
	public List<FileDto> findByBoardNum(Long boardNum) {
		return fileDao.findByBoardNum(boardNum);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteFiles(List<FileDto> deleteFiles) {
		if (deleteFiles == null || deleteFiles.isEmpty()) {
			return;
		}

		for (FileDto deleteFile : deleteFiles) {
			boolean isSuccess = fileDao.deleteByFileNum(deleteFile.getFileNum()) == Dbconstants.SUCCESS_CODE;
			if (!isSuccess) {
				throw new RuntimeException("fileService deleteFiles error");
			}
		}
	}

	@Override
	public int countByBoardNum(Long boardNum) {
		return fileDao.countByBoardNum(boardNum);
	}

	@Override
	public List<FileDto> findByYesterDay() {
		return fileDao.findByYesterDay();
	}

	@Override
	public boolean isImage(File file) {
		String contentType;
		try {
			contentType = Files.probeContentType(file.toPath());
			return contentType != null && contentType.startsWith("image/");
		} catch (IOException e) {
			throw new RuntimeException("fileService isImage error");
		}
	}

	@Override
	public String getContentType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			if (contentType != null && !contentType.startsWith("image/")) {
				throw new RuntimeException("no image error");
			}

			if (contentType == null) {
				String fileName = file.getName().toLowerCase();
				int dot = fileName.indexOf(".");
				if (dot <= 0 || dot == fileName.length() - 1) {
					throw new RuntimeException("no extension error");
				}

				String extension = fileName.substring(dot + 1);
				switch (extension) {
				case "jpg":
				case "jpeg":
					contentType = "image/jpeg";
					break;
				case "png":
					contentType = "image/png";
					break;
				case "gif":
					contentType = "image/gif";
					break;
				default:
					throw new RuntimeException("no match extension error");
				}
			}
			return contentType;
		} catch (IOException e) {
			throw new RuntimeException("fileService getContentType error", e);
		}
	}

	@Override
	public File getUploadPath(String parent, String child) {
		File uploadPath = new File(parent, child);
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		return uploadPath;
	}

	@Override
	public void createThumbnails(MultipartFile multipartFile, File thumbnailFile, int size) {
		try (InputStream in = multipartFile.getInputStream(); OutputStream out = new FileOutputStream(thumbnailFile);) {
			Thumbnailator.createThumbnail(in, out, size, size);
		} catch (Exception e) {
			throw new RuntimeException("fileService createThumbnails error");
		}
	}

	@Override
	public FileDto upload(MultipartFile multipartFile, String directoryPath, String datePath) {
		String fileUuid = UUID.randomUUID().toString();
		String originalFileName = multipartFile.getOriginalFilename();
		String fileName = fileUuid + "_" + originalFileName;
		File uploaPath = getUploadPath(directoryPath, datePath);
		File file = new File(uploaPath, fileName);

		try {
			multipartFile.transferTo(file);
			FileDto fileDto = new FileDto();
			fileDto.setFileUuid(fileUuid);
			fileDto.setFileUploadPath(datePath);
			fileDto.setFileName(originalFileName);
			fileDto.setFileSize(multipartFile.getSize());
			if (isImage(file)) {
				fileDto.setFileType(true);
				createThumbnails(multipartFile, new File(uploaPath, "t_" + fileName), THUMBNAIL_SIZE);
			}
			return fileDto;
		} catch (IllegalStateException | IOException e) {
			throw new RuntimeException("fileService upload error");
		}
	}

	@Override
	public String getFilePath(FileDto fileDto) {
		return getFileThumbnailPath(fileDto).replace("t_", "");
	}

	@Override
	public String getFileThumbnailPath(FileDto fileDto) {
		return fileDto.getFileUploadPath() + "/t_" + fileDto.getFileUuid() + "_" + fileDto.getFileName();
	}

	@Override
	public void setFilePath(FileDto fileDto) {
		fileDto.setFilePath(getFilePath(fileDto));
	}

	@Override
	public void autoDeleteFiles(List<FileDto> yesterdayFiles, String directoryPath, String yesterdayPath) {
		List<Path> paths = new ArrayList<>();
		yesterdayFiles.stream().map(file -> Paths.get(directoryPath, getFilePath(file))).forEach(paths::add);
		yesterdayFiles.stream().map(file -> Paths.get(directoryPath, getFileThumbnailPath(file))).forEach(paths::add);
		File[] files = new File(directoryPath, yesterdayPath).listFiles();
		files = files == null ? new File[0] : files;
		Arrays.stream(files).filter(file -> !paths.contains(file.toPath())).forEach(File::delete);
	}

}
