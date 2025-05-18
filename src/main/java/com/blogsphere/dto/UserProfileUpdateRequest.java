package com.blogsphere.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private String name;
    private MultipartFile profilePicture;
}