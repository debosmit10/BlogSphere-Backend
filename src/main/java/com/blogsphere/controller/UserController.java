package com.blogsphere.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsphere.dto.UserProfileResponse;
import com.blogsphere.dto.UserProfileUpdateRequest;
import com.blogsphere.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
 private final UserService userService;

 @PutMapping(value = "/{userId}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 public ResponseEntity<UserProfileResponse> updateProfile(
         @PathVariable Long userId,
         @ModelAttribute UserProfileUpdateRequest request,
         @AuthenticationPrincipal UserDetails currentUser) {
     return ResponseEntity.ok(
         userService.updateUserProfile(userId, request, currentUser)
     );
 }

 @GetMapping("/{userId}/profile")
 public ResponseEntity<UserProfileResponse> getProfile(
         @PathVariable Long userId) {
     return ResponseEntity.ok(userService.getUserProfile(userId));
 }
}