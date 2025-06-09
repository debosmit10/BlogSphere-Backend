package com.blogsphere.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogsphere.dto.UserRequestDTO;
import com.blogsphere.dto.UserResponseDTO;
import com.blogsphere.model.Role;
import com.blogsphere.model.User;
import com.blogsphere.repository.UserRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Service
@Builder
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO register(UserRequestDTO request) {
        User user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        User saved = userRepository.save(user);

        return UserResponseDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .role(saved.getRole())
                .build();
    }
}
