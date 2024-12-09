package com.EduLink.config.filestorage;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileStorageInterface {

	String saveFile(String originalFilename, String contentType, byte[] bytes);

	@Deprecated()
	File getStorageFileFromStorageId(String storageId);

	byte[] getStorageByteArrayFromStorageId(String storageId);

	void deleteStorageFile(String storageId);

	String saveFile(MultipartFile multipartFile);

}
