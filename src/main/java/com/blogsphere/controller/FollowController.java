package com.blogsphere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsphere.service.FollowService;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/{followerId}/following/{followingId}")
    public ResponseEntity<Boolean> toggleFollow(@PathVariable Long followerId, @PathVariable Long followingId) {
        boolean isFollowing = followService.toggleFollow(followerId, followingId);
        return ResponseEntity.ok(isFollowing);
    }

    @GetMapping("/{followerId}/following/{followingId}/status")
    public ResponseEntity<Boolean> getFollowStatus(@PathVariable Long followerId, @PathVariable Long followingId) {
        boolean isFollowing = followService.isFollowing(followerId, followingId);
        return ResponseEntity.ok(isFollowing);
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Long> getFollowerCount(@PathVariable Long userId) {
        long count = followService.getFollowerCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Long> getFollowingCount(@PathVariable Long userId) {
        long count = followService.getFollowingCount(userId);
        return ResponseEntity.ok(count);
    }
}