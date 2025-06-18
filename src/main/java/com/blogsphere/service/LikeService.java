package com.blogsphere.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.model.Blog;
import com.blogsphere.model.Like;
import com.blogsphere.model.User;
import com.blogsphere.repository.BlogRepository;
import com.blogsphere.repository.LikeRepository;
import com.blogsphere.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Transactional
    public void toggleLike(Long blogId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));

        likeRepository.findByUserAndBlog(user, blog).ifPresentOrElse(
                like -> {
                    likeRepository.delete(like);
                    blog.getLikes().remove(like);
                },
                () -> {
                    Like like = Like.builder().user(user).blog(blog).build();
                    likeRepository.save(like);
                    blog.getLikes().add(like);
                }
        );
    }

    public boolean hasUserLikedBlog(Long blogId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        return likeRepository.existsByUserAndBlog(user, blog);
    }

    public int getLikeCount(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        return likeRepository.countByBlog(blog);
    }
}