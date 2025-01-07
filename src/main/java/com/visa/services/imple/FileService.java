package com.visa.services.imple;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.visa.modals.Visa;

@Service
public class FileService {

	@Value("${banner.image}")
	private String bannerImagePath;
	
	@Value("${banner.image.ft}")
	private String bannerImageFrontend;

	public Visa updateOrSaveProfilePic(Visa visa, MultipartFile file) {
		String fileName = file.getOriginalFilename();
	
		// rename the file
		try {
			InputStream inputStream = file.getInputStream();

			String fileSavePath = bannerImagePath + File.separator + visa.getId() + fileName;
//		    File fileToSave = new File(fileSavePath);
			File fileDirectory = new File(bannerImagePath + File.separator);
			// making directory
			if (!fileDirectory.exists()) {
				boolean mkdirs = fileDirectory.mkdirs();
				if (mkdirs) {
					System.out.println("Folders created");
				} else {
					System.err.println("Unable to create");
				}
			}

			// copying
			Files.copy(inputStream, Paths.get(fileSavePath), StandardCopyOption.REPLACE_EXISTING);
			visa.setBannerImage(bannerImageFrontend + "/" + visa.getId() + fileName);
			return visa;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}