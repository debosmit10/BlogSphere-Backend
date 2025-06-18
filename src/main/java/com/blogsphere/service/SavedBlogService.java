package com.blogsphere.service;

import com.blogsphere.dto.BlogResponse;
import com.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.model.Blog;
import com.blogsphere.model.SavedBlog;
import com.blogsphere.model.User;
import com.blogsphere.repository.BlogRepository;
import com.blogsphere.repository.SavedBlogRepository;
import com.blogsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavedBlogService {

    private final SavedBlogRepository savedBlogRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final BlogService blogService; // Ensure BlogService is correctly injected

    /**
     * Save a blog for a user
     * @param blogId The ID of the blog to save
     * @param username The username of the user
     * @return True if the blog was saved, false if it was already saved
     */
    @Transactional
    public boolean saveBlog(Long blogId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found: " + blogId));
        
        // Check if already saved
        if (savedBlogRepository.existsByUserAndBlog(user, blog)) {
            return false;
        }
        
        // Create and save the SavedBlog entity
        SavedBlog savedBlog = SavedBlog.builder()
                .user(user)
                .blog(blog)
                .savedAt(LocalDateTime.now())
                .build();
        
        savedBlogRepository.save(savedBlog);
        return true;
    }
    
    /**
     * Unsave a blog for a user
     * @param blogId The ID of the blog to unsave
     * @param username The username of the user
     * @return True if the blog was unsaved, false if it wasn't saved
     */
    @Transactional
    public boolean unsaveBlog(Long blogId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found: " + blogId));
        
        // Check if saved
        if (!savedBlogRepository.existsByUserAndBlog(user, blog)) {
            return false;
        }
        
        savedBlogRepository.deleteByUserAndBlog(user, blog);
        return true;
    }
    
    /**
     * Toggle saved status for a blog
     * @param blogId The ID of the blog to toggle
     * @param username The username of the user
     * @return True if the blog is now saved, false if it is now unsaved
     */
    @Transactional
    public boolean toggleSavedStatus(Long blogId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found: " + blogId));
        
        // Check if already saved
        if (savedBlogRepository.existsByUserAndBlog(user, blog)) {
            // Unsave
            savedBlogRepository.deleteByUserAndBlog(user, blog);
            return false;
        } else {
            // Save
            SavedBlog savedBlog = SavedBlog.builder()
                    .user(user)
                    .blog(blog)
                    .savedAt(LocalDateTime.now())
                    .build();
            
            savedBlogRepository.save(savedBlog);
            return true;
        }
    }
    
    /**
     * Check if a blog is saved by a user
     * @param blogId The ID of the blog to check
     * @param username The username of the user
     * @return True if the blog is saved, false otherwise
     */
    public boolean isBlogSaved(Long blogId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found: " + blogId));
        
        return savedBlogRepository.existsByUserAndBlog(user, blog);
    }
    
    /**
     * Get all saved blogs for a user
     * @param username The username of the user
     * @return List of BlogResponse objects
     */
    public List<BlogResponse> getSavedBlogs(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        
        List<SavedBlog> savedBlogs = savedBlogRepository.findByUserOrderBySavedAtDesc(user);
        
        // Use the mapToBlogResponse method from BlogService
        return savedBlogs.stream()
                .map(savedBlog -> blogService.mapToBlogResponse(savedBlog.getBlog(), username))
                .collect(Collectors.toList());
    }
}
