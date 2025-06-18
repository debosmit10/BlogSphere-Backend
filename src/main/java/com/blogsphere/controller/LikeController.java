package com.blogsphere.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsphere.service.LikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/blogs/{blogId}/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> toggleLike(
            @PathVariable Long blogId,
            @AuthenticationPrincipal UserDetails userDetails) {
        likeService.toggleLike(blogId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> getLikeStatus(
            @PathVariable Long blogId,
            @AuthenticationPrincipal UserDetails userDetails) {
        boolean isLiked = likeService.hasUserLikedBlog(blogId, userDetails.getUsername());
        return ResponseEntity.ok(isLiked);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long blogId) {
        int count = likeService.getLikeCount(blogId);
        return ResponseEntity.ok(count);
    }
}