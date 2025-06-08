package com.blogsphere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsphere.service.CommentLikeService;

@RestController
@RequestMapping("/api/comment-likes")
public class CommentLikeController {

    @Autowired
    private CommentLikeService commentLikeService;

    @PostMapping("/{commentId}/users/{userId}")
    public ResponseEntity<Boolean> toggleCommentLike(@PathVariable Long commentId, @PathVariable Long userId) {
        boolean liked = commentLikeService.toggleCommentLike(commentId, userId);
        return new ResponseEntity<>(liked, HttpStatus.OK);
    }

    @GetMapping("/{commentId}/count")
    public ResponseEntity<Long> getCommentLikeCount(@PathVariable Long commentId) {
        long count = commentLikeService.getCommentLikeCount(commentId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/{commentId}/status/users/{userId}")
    public ResponseEntity<Boolean> getCommentLikeStatus(@PathVariable Long commentId, @PathVariable Long userId) {
        boolean liked = commentLikeService.hasUserLikedComment(commentId, userId);
        return new ResponseEntity<>(liked, HttpStatus.OK);
    }
}