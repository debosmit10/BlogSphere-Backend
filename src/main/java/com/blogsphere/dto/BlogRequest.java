package com.blogsphere.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import com.blogsphere.model.Topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {
	@NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Topic is required")
    private String topic;  // Keep as String for form-data binding

    private MultipartFile imageFile;

    // No need for toUpperCase() here since we'll handle it in service
    public Topic getAsTopic() {
        if (this.topic == null) {
            throw new IllegalArgumentException("Topic cannot be null");
        }
        return Topic.valueOf(this.topic);
    }
}