package com.blogsphere.dto;

import java.time.LocalDateTime;

import com.blogsphere.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String authorName;
    private String authorUsername;
}