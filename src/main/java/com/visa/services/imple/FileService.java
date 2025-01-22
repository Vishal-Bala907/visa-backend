package com.visa.services.imple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.visa.modals.ImageUpdateDTO;
import com.visa.modals.Visa;
import com.visa.repos.VisaRepo;

@Service
public class FileService {

	@Value("${file.upload-dir}")
	private String uploadDir;
	@Autowired
	private VisaRepo repo;

	public Visa updateOrSaveProfilePic(Visa visa, MultipartFile file) {
		String fileName = file.getOriginalFilename();

		try (InputStream inputStream = file.getInputStream()) {
			// Resolve the relative path based on the working directory
//			System.out.println(System.getProperty("user.dir"));
			Path fileSavePath = Paths.get(System.getProperty("user.dir"), uploadDir, visa.getId() + "_" + fileName);
			File fileDirectory = fileSavePath.getParent().toFile();

			// Create directories if they don't exist
			if (!fileDirectory.exists() && !fileDirectory.mkdirs()) {
				throw new IOException("Failed to create directory: " + fileDirectory.getAbsolutePath());
			}

			// Save the file
			Files.copy(inputStream, fileSavePath, StandardCopyOption.REPLACE_EXISTING);
			visa.setBannerImage("images/" + visa.getId() + "_" + fileName);

			return visa;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String uploadImage(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		try (InputStream inputStream = file.getInputStream()) {
			final String  FILENAME = Math.random()  + Math.random() + "_" + fileName;
			Path fileSavePath = Paths.get(System.getProperty("user.dir"), uploadDir,FILENAME);
			File fileDirectory = fileSavePath.getParent().toFile();
			
			if(!fileDirectory.exists() && !fileDirectory.mkdirs()) {
				throw new IOException("Failed to create directory: " + fileDirectory.getAbsolutePath());
			}
			
			Files.copy(inputStream, fileSavePath, StandardCopyOption.REPLACE_EXISTING);
			return "images/" + FILENAME;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String uploadBlogImage(MultipartFile file) {
		String fileName = file.getOriginalFilename();

		try (InputStream inputStream = file.getInputStream()) {
			// Resolve the relative path based on the working directory
			String savedFile = Math.random() + "_" + fileName;
//			System.out.println(System.getProperty("user.dir"));
			Path fileSavePath = Paths.get(System.getProperty("user.dir"), uploadDir, savedFile);
			File fileDirectory = fileSavePath.getParent().toFile();

			// Create directories if they don't exist
			if (!fileDirectory.exists() && !fileDirectory.mkdirs()) {
				throw new IOException("Failed to create directory: " + fileDirectory.getAbsolutePath());
			}

			// Save the file
			Files.copy(inputStream, fileSavePath, StandardCopyOption.REPLACE_EXISTING);
			return "images/"+savedFile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// handle base64 image
	public boolean handleBase64Image(Visa visa, ImageUpdateDTO dto) {

		// base64 image string
		String base64 = dto.getImage();
		String originalName = dto.getOriginalName();
		String savedImageName = Math.random() + visa.getId() + originalName;

		try {
			byte[] image = Base64.getDecoder().decode(base64);
			String filePath = uploadDir + File.separator + savedImageName;

			// delete the old image
			deleteImageByVisaId(visa.getBannerImage());

			// save the new image path to the db
			visa.setBannerImage("images/" + savedImageName);
			// save the changes
			repo.save(visa);

			try {
				FileOutputStream fos = new FileOutputStream(filePath);
				fos.write(image);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void deleteImageByVisaId(String img) throws Exception {
		// Fetch the image details from the database (if needed)
		// For example:
		// String imageName = imageRepository.findImageNameByVisaId(visaId);

		// Assuming the image filename is derived from the visaId
		String imageName = img.split("/")[1]; // Adjust based on your naming convention
		Path imagePath = Paths.get(uploadDir, imageName);

		// Delete the image file
		if (Files.exists(imagePath)) {
			Files.delete(imagePath);
		} else {
			throw new Exception("Image not found");
		}

		// Optionally, remove the image record from the database
		// imageRepository.deleteByVisaId(visaId);
	}
}
