package com.blogsphere.controller;

import com.blogsphere.dto.BlogResponse;
import com.blogsphere.service.SavedBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blogs/saved")
@RequiredArgsConstructor
public class SavedBlogController {

    private final SavedBlogService savedBlogService;

    /**
     * Toggle saved status for a blog
     */
    @PostMapping("/{blogId}")
    public ResponseEntity<Map<String, Boolean>> toggleSavedStatus(
            @PathVariable Long blogId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        boolean isSaved = savedBlogService.toggleSavedStatus(blogId, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("saved", isSaved));
    }

    /**
     * Check if a blog is saved by the current user
     */
    @GetMapping("/{blogId}/status")
    public ResponseEntity<Map<String, Boolean>> checkSavedStatus(
            @PathVariable Long blogId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        boolean isSaved = savedBlogService.isBlogSaved(blogId, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("saved", isSaved));
    }

    /**
     * Get all saved blogs for the current user
     */
    @GetMapping
    public ResponseEntity<List<BlogResponse>> getSavedBlogs(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        List<BlogResponse> savedBlogs = savedBlogService.getSavedBlogs(userDetails.getUsername());
        return ResponseEntity.ok(savedBlogs);
    }
}
