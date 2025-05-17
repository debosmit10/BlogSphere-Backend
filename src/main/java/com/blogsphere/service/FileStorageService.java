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

@Service
public class FileStorageService {
 
	private final Path blogImagesLocation;
    private final Path profilePicturesLocation;
 
 public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
	 
	 // Initialize directories
     this.blogImagesLocation = Paths.get(uploadDir, "blog-images").toAbsolutePath().normalize();
     this.profilePicturesLocation = Paths.get(uploadDir, "profile-pictures").toAbsolutePath().normalize();
     
     try {
    	 Files.createDirectories(blogImagesLocation);
         Files.createDirectories(profilePicturesLocation);
     } catch (IOException e) {
         throw new RuntimeException("Could not initialize storage directories", e);
     }
 }
 
 public String storeBlogImage(MultipartFile file) {
     return storeFile(file, blogImagesLocation);
 }

 public String storeProfilePicture(MultipartFile file) {
     return storeFile(file, profilePicturesLocation);
 }
 
 public String storeFile(MultipartFile file, Path location) {
     try {
         if (file.isEmpty()) {
             throw new RuntimeException("Failed to store empty file");
         }
         
         String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();	// File name generated here
         Path destinationFile = location.resolve(filename)
             .normalize()
             .toAbsolutePath();
         
         Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
         return filename;
     } catch (IOException e) {
         throw new RuntimeException("Failed to store file", e);
     }
 }
 
 public Resource loadBlogImage(String filename) {
     return loadFile(filename, blogImagesLocation);
 }

 public Resource loadProfilePicture(String filename) {
     return loadFile(filename, profilePicturesLocation);
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
 
 public void deleteBlogImage(String filename) {
     deleteFile(filename, blogImagesLocation);
 }

 public void deleteProfilePicture(String filename) {
     deleteFile(filename, profilePicturesLocation);
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