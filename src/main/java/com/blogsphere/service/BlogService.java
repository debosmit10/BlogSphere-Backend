package com.blogsphere.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.blogsphere.dto.BlogRequest;
import com.blogsphere.dto.BlogResponse;
import com.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.model.Blog;
import com.blogsphere.model.Topic;
import com.blogsphere.model.User;
import com.blogsphere.repository.BlogRepository;
import com.blogsphere.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService; // NEW

    public BlogResponse createBlog(BlogRequest request, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Convert topic string to enum
        Topic topic;
        try {
            topic = Topic.valueOf(request.getTopic().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid topic: " + request.getTopic());
        }
        
        String imageUrl = null;
        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            imageUrl = fileStorageService.storeBlogImage(request.getImageFile());
        }
        
        Blog blog = Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .topic(topic) // Convert to enum here
                .imageUrl(imageUrl)
                .author(author)
                .createdAt(LocalDateTime.now())
                .build();

        Blog savedBlog = blogRepository.save(blog);
        return mapToBlogResponse(savedBlog);
    }

    public BlogResponse getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        return mapToBlogResponse(blog);
    }

    public List<BlogResponse> getAllBlogs() {
        return blogRepository.findAll().stream()
                .map(this::mapToBlogResponse)
                .collect(Collectors.toList());
    }

    public List<BlogResponse> getBlogsByUser(String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return blogRepository.findByAuthorId(author.getId()).stream()
                .map(this::mapToBlogResponse)
                .collect(Collectors.toList());
    }

    public BlogResponse updateBlog(Long id, BlogRequest request, UserDetails userDetails) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));

        // Check if the current user is the author
        if (!blog.getAuthor().getUsername().equals(userDetails.getUsername())) {
            throw new RuntimeException("You are not authorized to update this blog");
        }

        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        
        // Handle image update
        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            try {
                // Delete old image if exists
                if (blog.getImageUrl() != null) {
                    fileStorageService.deleteBlogImage(blog.getImageUrl());
                }
                // Store new image
                blog.setImageUrl(fileStorageService.storeBlogImage(request.getImageFile()));
            } catch (RuntimeException e) {
                throw new RuntimeException("Failed to update image: " + e.getMessage());
            }
        }
        
        Blog updatedBlog = blogRepository.save(blog);
        return mapToBlogResponse(updatedBlog);
    }

    public void deleteBlog(Long id, UserDetails userDetails) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));

        // Check if the current user is the author
        if (!blog.getAuthor().getUsername().equals(userDetails.getUsername())) {
            throw new RuntimeException("You are not authorized to delete this blog");
        }
        
        // Delete associated image if exists
        if (blog.getImageUrl() != null) {
            fileStorageService.deleteBlogImage(blog.getImageUrl());
        }

        blogRepository.delete(blog);
    }

    private BlogResponse mapToBlogResponse(Blog blog) {
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .topic(blog.getTopic())
                .topicDisplayName(blog.getTopic().getDisplayName())
                .imageUrl(blog.getImageUrl())
                .createdAt(blog.getCreatedAt())
                .authorName(blog.getAuthor().getName())
                .authorUsername(blog.getAuthor().getUsername())
                .authorProfilePictureUrl(blog.getAuthor().getProfilePictureUrl())
                .build();
    }
    
    public List<BlogResponse> getBlogsByTopic(Topic topic) {
        return blogRepository.findByTopic(topic).stream()
                .map(this::mapToBlogResponse)
                .collect(Collectors.toList());
    }
}