package com.blogsphere.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blogsphere.model.Blog;
import com.blogsphere.model.User;
import com.blogsphere.model.VisitedBlog;
import com.blogsphere.repository.BlogRepository;
import com.blogsphere.repository.UserRepository;
import com.blogsphere.repository.VisitedBlogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisitedBlogService {

    private final VisitedBlogRepository visitedBlogRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;

    @Transactional
    public void recordVisitedBlog(Long userId, Long blogId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        Optional<VisitedBlog> existingVisitedBlog = visitedBlogRepository.findByUserAndBlog(user, blog);

        if (existingVisitedBlog.isPresent()) {
            VisitedBlog visitedBlog = existingVisitedBlog.get();
            visitedBlog.setVisitedAt(LocalDateTime.now());
            visitedBlogRepository.save(visitedBlog);
        } else {
            VisitedBlog newVisitedBlog = VisitedBlog.builder()
                    .user(user)
                    .blog(blog)
                    .visitedAt(LocalDateTime.now())
                    .build();
            visitedBlogRepository.save(newVisitedBlog);
        }
    }

    public List<VisitedBlog> getRecentlyVisitedBlogs(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return visitedBlogRepository.findByUserOrderByVisitedAtDesc(user);
    }
}