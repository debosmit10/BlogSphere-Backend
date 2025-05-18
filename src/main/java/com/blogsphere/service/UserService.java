package com.blogsphere.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.blogsphere.dto.UserProfileResponse;
import com.blogsphere.dto.UserProfileUpdateRequest;
import com.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.model.User;
import com.blogsphere.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
 private final UserRepository userRepository;
 private final FileStorageService fileStorageService;

 public UserProfileResponse getUserProfile(Long userId) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    
	    return mapToProfileResponse(user);
	}
 
 public UserProfileResponse updateUserProfile(Long userId, 
                                           UserProfileUpdateRequest request,
                                           UserDetails currentUser) {
     User user = userRepository.findById(userId)
             .orElseThrow(() -> new ResourceNotFoundException("User not found"));
     
     // Verify the current user can only update their own profile
     if (!user.getUsername().equals(currentUser.getUsername())) {
         throw new RuntimeException("Unauthorized profile update");
     }

     if (request.getName() != null) {
         user.setName(request.getName());
     }

     if (request.getProfilePicture() != null && !request.getProfilePicture().isEmpty()) {
         // Delete old profile picture if exists
         if (user.getProfilePictureUrl() != null) {
             fileStorageService.deleteProfilePicture(user.getProfilePictureUrl());
         }
         // Store new profile picture
         String filename = fileStorageService.storeProfilePicture(request.getProfilePicture());
         user.setProfilePictureUrl(filename);
     }

     User updatedUser = userRepository.save(user);
     return mapToProfileResponse(updatedUser);
 }

 private UserProfileResponse mapToProfileResponse(User user) {
     return UserProfileResponse.builder()
             .id(user.getId())
             .name(user.getName())
             .username(user.getUsername())
             .email(user.getEmail())
             .profilePictureUrl(user.getProfilePictureUrl())
             .role(user.getRole())
             .build();
 }
}