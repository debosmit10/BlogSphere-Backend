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
 
 private final Path rootLocation;
 
 public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
     this.rootLocation = Paths.get(uploadDir);
     try {
         Files.createDirectories(rootLocation);
     } catch (IOException e) {
         throw new RuntimeException("Could not initialize storage", e);
     }
 }
 
 public String storeFile(MultipartFile file) {
     try {
         if (file.isEmpty()) {
             throw new RuntimeException("Failed to store empty file");
         }
         
         String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
         Path destinationFile = this.rootLocation.resolve(filename)
             .normalize()
             .toAbsolutePath();
         
         Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
         return filename;
     } catch (IOException e) {
         throw new RuntimeException("Failed to store file", e);
     }
 }
 
 public Resource loadFile(String filename) {
     try {
         Path file = rootLocation.resolve(filename);
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
 
 public void deleteFile(String filename) {
	    try {
	        Path file = rootLocation.resolve(filename);
	        Files.deleteIfExists(file);
	    } catch (IOException e) {
	        throw new RuntimeException("Failed to delete file: " + filename, e);
	    }
	}
}