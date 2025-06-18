package com.blogsphere.dto;

import com.blogsphere.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserResponseDTO {
	private Long id;
    private String name;
    private String username;
    private String email;
    private Role role;
    private String profilePictureUrl;
}
