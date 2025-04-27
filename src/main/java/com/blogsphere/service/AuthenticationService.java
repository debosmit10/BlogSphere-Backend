package com.blogsphere.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogsphere.dto.AuthenticationRequest;
import com.blogsphere.dto.AuthenticationResponse;
import com.blogsphere.dto.RegisterRequest;
import com.blogsphere.exception.DuplicateResourceException;
import com.blogsphere.model.Role;
import com.blogsphere.model.User;
import com.blogsphere.repository.UserRepository;
import com.blogsphere.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
    	// Check for existing username or email
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already taken");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }
    	
    	var user = User.builder()
            .name(request.getName())
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.ROLE_USER)
            .build();
        
        userRepository.save(user);
        
        // Generate token
        var jwtToken = jwtUtil.generateToken(user);
        
        return buildAuthResponse(user, jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
    	// Authenticate credentials
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()));
        
        // Get user from database
        var user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Generate token
        var jwtToken = jwtUtil.generateToken(user);
        
        return buildAuthResponse(user, jwtToken);
    }
    
    private AuthenticationResponse buildAuthResponse(User user, String token) {
        return AuthenticationResponse.builder()
            .token(token)
            .userId(user.getId())
            .username(user.getUsername())
            .name(user.getName())
            .email(user.getEmail())
            .role(user.getRole())
            .build();
    }
}