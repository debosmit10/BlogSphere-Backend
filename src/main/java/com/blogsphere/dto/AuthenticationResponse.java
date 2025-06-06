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
public class AuthenticationResponse {
    private String token;
    private Long userId;
    private String name;
    private String username;
    private String email;
    private Role role;
    private String profilePictureUrl;
}