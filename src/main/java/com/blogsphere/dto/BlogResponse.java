package com.blogsphere.dto;

import java.time.LocalDateTime;

import com.blogsphere.model.Role;
import com.blogsphere.model.Topic;

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
    private String imageUrl;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;
    private boolean isLikedByCurrentUser;
    private Long authorId;
    private String authorName;
    private String authorUsername;
    private String authorProfilePictureUrl;
    private Role authorRole;
    private Topic topic;
    private String topicDisplayName;
    private boolean isSavedByCurrentUser;
}