package com.blogsphere.dto;

import com.blogsphere.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
	private Long id;
    private String name;
    private String username;
    private String email;
    private String profilePictureUrl;
    private Role role;
}