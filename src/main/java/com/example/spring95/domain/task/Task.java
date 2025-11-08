package com.example.spring95.domain.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.spring95.common.DateUtil;
import com.example.spring95.domain.service.file.FileService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Task {
	private final FileService fileService;
	private static final String DEFAULT_DIRECTORY = "C:/upload/files";
	private static final String DOWNLOAD_DIRECTORY = "C:/upload/download";

	/* @Scheduled(cron = "0 * * * * ?") */
	public void autoDeleteFiles() {
		fileService.autoDeleteFiles(fileService.findByYesterDay(), DOWNLOAD_DIRECTORY, DateUtil.getYesterDayPath());
	}

}
