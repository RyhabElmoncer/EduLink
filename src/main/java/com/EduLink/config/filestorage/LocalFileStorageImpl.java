package com.EduLink.config.filestorage;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class LocalFileStorageImpl implements FileStorageInterface {
    private static final Logger logger = LoggerFactory.getLogger(LocalFileStorageImpl.class);

	private String localPath;

	public LocalFileStorageImpl(String localPath) {
		this.localPath = localPath;
	}

	@Override
	public String saveFile(String originalFilename, String contentType, byte[] bytes) {
		String fileId = UUID.randomUUID().toString();
		String absoluteFileName = localPath + File.separator + fileId + "-" + originalFilename;
		logger.info("");
//		File file = new File(absoluteFileName);

//			file.createNewFile();
		try (FileOutputStream fos = new FileOutputStream(absoluteFileName)) {
			// Write the byte array to the file
			fos.write(bytes);
 
		} catch (IOException e) {
			// Handle any IO exceptions
			System.err.println("An error occurred while writing to the file: " + e.getMessage());
		}

		return absoluteFileName;
	}

	@Override
	public File getStorageFileFromStorageId(String absoluteFileName) {
		// TODO Auto-generated method stub
		return new File(absoluteFileName);
	}

	@Override
	public void deleteStorageFile(String absoluteFileName) {
		File file = new File(absoluteFileName);
		FileUtils.deleteQuietly(file);

	}

	@Override
	public String saveFile(MultipartFile multipartFile) {
		try {
			return saveFile(multipartFile.getOriginalFilename(),multipartFile.getContentType(),multipartFile.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public byte[] getStorageByteArrayFromStorageId(String storageId) {
		// TODO Auto-generated method stub
		return null;
	}

 

}
