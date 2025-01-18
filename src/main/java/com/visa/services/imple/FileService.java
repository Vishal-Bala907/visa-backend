package com.visa.services.imple;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.visa.modals.Visa;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Visa updateOrSaveProfilePic(Visa visa, MultipartFile file) {
        String fileName = file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream()) {
            // Resolve the relative path based on the working directory
        	System.out.println(System.getProperty("user.dir"));
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
