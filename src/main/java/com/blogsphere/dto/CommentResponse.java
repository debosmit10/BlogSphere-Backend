package com.blogsphere.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
	private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String username;
    private String userProfilePictureUrl;
}
