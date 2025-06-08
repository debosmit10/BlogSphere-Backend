package com.blogsphere.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogsphere.dto.CommentResponse;
import com.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.model.Blog;
import com.blogsphere.model.Comment;
import com.blogsphere.model.User;
import com.blogsphere.repository.BlogRepository;
import com.blogsphere.repository.CommentRepository;
import com.blogsphere.repository.UserRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    public Comment createComment(Long blogId, Long userId, String content) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id " + blogId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        Comment comment = new Comment();
        comment.setBlog(blog);
        comment.setUser(user);
        comment.setContent(content);
        // Set createdAt and updatedAt if needed, typically handled by @CreationTimestamp/@UpdateTimestamp

        return commentRepository.save(comment);
    }

    public List<CommentResponse> getCommentsByBlogId(Long blogId) {
        List<Comment> comments = commentRepository.findByBlogIdOrderByCreatedAtAsc(blogId);
        return comments.stream().map(comment -> CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .userId(comment.getUser().getId())
                .username(comment.getUser().getUsername())
                .userProfilePictureUrl(comment.getUser().getProfilePictureUrl())
                .build()
        ).toList();
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId));

        // Optional: Add authorization check if only the comment author can delete
        if (!comment.getUser().getId().equals(userId)) {
             throw new IllegalArgumentException("You are not authorized to delete this comment.");
        }

        commentRepository.delete(comment);
    }

    // Add other methods as needed, e.g., updateComment
}


