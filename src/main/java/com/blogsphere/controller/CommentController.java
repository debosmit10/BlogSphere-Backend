package com.blogsphere.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsphere.dto.CommentResponse;
import com.blogsphere.model.Comment;
import com.blogsphere.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/blogs/{blogId}/users/{userId}")
    public ResponseEntity<Comment> createComment(@PathVariable Long blogId, @PathVariable Long userId, @RequestBody String content) {
        Comment comment = commentService.createComment(blogId, userId, content);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping("/blogs/{blogId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByBlogId(@PathVariable Long blogId) {
        List<CommentResponse> comments = commentService.getCommentsByBlogId(blogId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/users/{userId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @PathVariable Long userId) {
        commentService.deleteComment(commentId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}