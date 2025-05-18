package com.blogsphere.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
 
 @GetMapping("/blog-images/{filename:.+}")
 public ResponseEntity<Resource> serveBlogImage(@PathVariable String filename) {
     Resource file = fileStorageService.loadBlogImage(filename);
     return ResponseEntity.ok()
             .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
             .body(file);
 }

 @GetMapping("/profile-pictures/{filename:.+}")
 public ResponseEntity<Resource> serveProfilePicture(@PathVariable String filename) {
     Resource file;
     
     try {
         if (filename.equals("default.jpg")) {
             file = fileStorageService.loadDefaultProfilePicture();
         } else {
             file = fileStorageService.loadProfilePicture(filename);
         }
         
         return ResponseEntity.ok()
             .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "inline; filename=\"" + file.getFilename() + "\"")
             .body(file);
     } catch (RuntimeException e) {
         // Fallback to default if image not found
         file = fileStorageService.loadDefaultProfilePicture();
         return ResponseEntity.ok()
             .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "inline; filename=\"default.jpg\"")
             .body(file);
     }
 }
}