package com.blogsphere.controller;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsphere.service.FileStorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
 
 private final FileStorageService fileStorageService;
 
 @GetMapping("/profile-pictures/default.jpg")
 public ResponseEntity<Resource> serveDefaultProfilePicture() {
     Resource file = fileStorageService.loadDefaultProfilePicture();
     return buildImageResponse(file, "default.jpg");
 }
 
 @GetMapping("/profile-pictures/{filename:.+}")
 public ResponseEntity<Resource> serveProfilePicture(@PathVariable String filename) {
     Resource file = filename.equals("default.jpg") 
         ? fileStorageService.loadDefaultProfilePicture()
         : fileStorageService.loadProfilePicture(filename);
     
     return buildImageResponse(file, filename);
 }
 
 @GetMapping("/blog-images/{filename:.+}")
 public ResponseEntity<Resource> serveBlogImage(@PathVariable String filename) {
     Resource file = fileStorageService.loadBlogImage(filename);
     return buildImageResponse(file, filename);
 }
 
 private ResponseEntity<Resource> buildImageResponse(Resource file, String filename) {
     try {
         String contentType = determineContentType(file, filename);
         return ResponseEntity.ok()
                 .header(HttpHeaders.CONTENT_TYPE, contentType)
                 .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000") // 1 year cache
                 .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                 .body(file);
     } catch (IOException e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
 }
 
 private String determineContentType(Resource file, String filename) throws IOException {
     // First try to determine from filename
     String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
     switch (extension) {
         case "jpg":
         case "jpeg": return "image/jpeg";
         case "png": return "image/png";
         case "gif": return "image/gif";
         case "svg": return "image/svg+xml";
     }
     
     // Fallback to Java's probeContentType if needed
     if (file.exists() && file.isReadable() && file.getFile() != null) {
         String probedType = Files.probeContentType(file.getFile().toPath());
         if (probedType != null) {
             return probedType;
         }
     }
     
     // Final fallback
     return "application/octet-stream";
 }
}