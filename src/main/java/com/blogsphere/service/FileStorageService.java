package com.blogsphere.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

@Service
public class FileStorageService {
 
	// File Storage Locations
	private final Path blogImagesLocation;
	private final Path profilePicturesLocation;
	
	@Value("${app.default-profile-picture}")
    private Resource defaultProfilePicture;
    
 // Constructor initializes storage directories
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) throws IOException {
	 
	 // Initialize directories
     this.blogImagesLocation = Paths.get(uploadDir, "blog-images").toAbsolutePath().normalize();
     this.profilePicturesLocation = Paths.get(uploadDir, "profile-pictures").toAbsolutePath().normalize();
     
     try {
    	 // Create directories if they don't exist
    	 Files.createDirectories(blogImagesLocation);
         Files.createDirectories(profilePicturesLocation);
     } catch (IOException e) {
         throw new RuntimeException("Could not initialize storage directories", e);
     }
 }
    
 // Initialize default profile picture after bean construction
    @PostConstruct
    public void setup() throws IOException {
        this.initializeDefaultProfilePicture();
    }
    
    private void initializeDefaultProfilePicture() throws IOException {
        Path defaultImagePath = profilePicturesLocation.resolve("default.jpg");
        if (!Files.exists(defaultImagePath)) {
            Files.copy(
                defaultProfilePicture.getInputStream(),
                defaultImagePath,
                StandardCopyOption.REPLACE_EXISTING
            );
        }
    }
 
 // ========== BLOG IMAGE METHODS ========== //  
 public String storeBlogImage(MultipartFile file) {
     return storeFile(file, blogImagesLocation, "image/");
 }
 
 public Resource loadBlogImage(String filename) {
     return loadFile(filename, blogImagesLocation);
 }
 
 public void deleteBlogImage(String filename) {
     deleteFile(filename, blogImagesLocation);
 }

 // ========== PROFILE PICTURE METHODS ========== //
 public String storeProfilePicture(MultipartFile file) {
     return storeFile(file, profilePicturesLocation, "image/");
 }
 
 public Resource loadProfilePicture(String filename) {
     return loadFile(filename, profilePicturesLocation);
 }
 
 public void deleteProfilePicture(String filename) {
     deleteFile(filename, profilePicturesLocation);
 }
 
 public Resource loadDefaultProfilePicture() {
     try {
         Path defaultImage = profilePicturesLocation.resolve("default.jpg");
         Resource resource = new UrlResource(defaultImage.toUri());
         if (resource.exists() || resource.isReadable()) {
             return resource;
         }
         throw new RuntimeException("Default profile picture missing");
     } catch (MalformedURLException e) {
         throw new RuntimeException("Error loading default profile picture", e);
     }
 }
 
 // ========== PRIVATE HELPER METHODS ========== //
 public String storeFile(MultipartFile file, Path location, String expectedContentType) {
     try {
    	 // Validate file
         if (file.isEmpty()) {
             throw new RuntimeException("Failed to store empty file");
         }
         
         // Validate content type
         if (expectedContentType != null && !file.getContentType().startsWith(expectedContentType)) {
             throw new RuntimeException("Only " + expectedContentType + " files are allowed");
         }
         
         // Generate unique filename
         String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
         Path destinationFile = location.resolve(filename)
             .normalize()
             .toAbsolutePath();
         
         // Save file
         Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
         return filename;
     } catch (IOException e) {
         throw new RuntimeException("Failed to store file", e);
     }
 }
 
 public Resource loadFile(String filename, Path location) {
     try {
         Path file = location.resolve(filename);
         Resource resource = new UrlResource(file.toUri());
         
         if (resource.exists() || resource.isReadable()) {
             return resource;
         } else {
             throw new RuntimeException("Could not read file: " + filename);
         }
     } catch (MalformedURLException e) {
         throw new RuntimeException("Could not read file: " + filename, e);
     }
 }
 
 public void deleteFile(String filename, Path location) {
	    try {
	        Path file = location.resolve(filename);
	        Files.deleteIfExists(file);
	    } catch (IOException e) {
	        throw new RuntimeException("Failed to delete file: " + filename, e);
	    }
	}
}