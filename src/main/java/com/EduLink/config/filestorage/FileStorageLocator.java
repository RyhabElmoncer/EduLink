package com.EduLink.config.filestorage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class FileStorageLocator {
	@Value("${localPath}")
	private String localPath;

	@Bean
	public FileStorageInterface getInstance() {
		return new LocalFileStorageImpl(localPath);
		// return new AlfrescoStorageImpl(localPath);
	}

}
