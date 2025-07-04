package com.blogsphere.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsphere.dto.BlogRequest;
import com.blogsphere.dto.BlogResponse;
import com.blogsphere.model.Topic;
import com.blogsphere.model.User;
import com.blogsphere.repository.UserRepository;
import com.blogsphere.service.BlogService;
import com.blogsphere.service.VisitedBlogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final VisitedBlogService visitedBlogService;
    private final UserRepository userRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBlog(
            @Valid @ModelAttribute BlogRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            return ResponseEntity.ok(blogService.createBlog(request, userDetails.getUsername()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponse> getBlogById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        BlogResponse blogResponse = blogService.getBlogById(id, username);
        if (userDetails != null) {
            User currentUser = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
            if (currentUser != null) {
                visitedBlogService.recordVisitedBlog(currentUser.getId(), id);
            }
        }
        return ResponseEntity.ok(blogResponse);
    }

    @GetMapping
    public ResponseEntity<List<BlogResponse>> getAllBlogs(
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(blogService.getAllBlogs(username));
    }
    
    @GetMapping("/topic/{topic}")
    public ResponseEntity<List<BlogResponse>> getBlogsByTopic(
            @PathVariable Topic topic,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(blogService.getBlogsByTopic(topic, username));
    }
    
    @GetMapping("/topics")
    public ResponseEntity<List<Map<String, String>>> getAllTopics() {
        List<Map<String, String>> topics = Arrays.stream(Topic.values())
                .map(topic -> Map.of(
                        "name", topic.name(),
                        "displayName", topic.getDisplayName()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/my-blogs")
    public ResponseEntity<List<BlogResponse>> getUserBlogs(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(blogService.getBlogsByUser(
            userDetails.getUsername(), 
            userDetails.getUsername()
        ));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BlogResponse>> getBlogsByUserId(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(blogService.getBlogsByUserId(userId, username));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BlogResponse> updateBlog(
            @PathVariable Long id,
            @ModelAttribute BlogRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(blogService.updateBlog(id, request, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        blogService.deleteBlog(id, userDetails);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/following")
    public ResponseEntity<List<BlogResponse>> getBlogsFromFollowedUsers(
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(blogService.getBlogsFromFollowedUsers(username));
    }

    @GetMapping("/top-liked-weekly")
    public ResponseEntity<List<BlogResponse>> getTopLikedBlogsOfWeek(
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(blogService.getTopLikedBlogsOfWeek(username));
    }

    @GetMapping("/recently-visited")
    public ResponseEntity<List<BlogResponse>> getRecentlyVisitedBlogs(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().build();
        }
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(blogService.getRecentlyVisitedBlogs(currentUser.getId(), userDetails.getUsername()));
    }
}