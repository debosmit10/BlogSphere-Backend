package com.blogsphere.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.blogsphere.dto.BlogRequest;
import com.blogsphere.dto.BlogResponse;
import com.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.model.Blog;
import com.blogsphere.model.Topic;
import com.blogsphere.model.User;
import com.blogsphere.repository.BlogRepository;
import com.blogsphere.repository.SavedBlogRepository;
import com.blogsphere.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final LikeService likeService;
    private final SavedBlogRepository savedBlogRepository;
    private final FollowService followService;
    private final VisitedBlogService visitedBlogService;
    private final CommentService commentService;

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
        return mapToBlogResponse(savedBlog, username);
    }

    public BlogResponse getBlogById(Long id, String currentUsername) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        return mapToBlogResponse(blog, currentUsername);
    }

    public List<BlogResponse> getAllBlogs(String currentUsername) {
        return blogRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
        		.map(blog -> mapToBlogResponse(blog, currentUsername))
                .collect(Collectors.toList());
    }

    public List<BlogResponse> getBlogsByUser(String username, String currentUsername) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return blogRepository.findByAuthorId(author.getId()).stream()
        		.map(blog -> mapToBlogResponse(blog, currentUsername))
                .collect(Collectors.toList());
    }
    
    public List<BlogResponse> getBlogsByUserId(Long userId, String currentUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Blog> blogs = blogRepository.findByAuthorOrderByCreatedAtDesc(user);
        return blogs.stream()
                .map(blog -> mapToBlogResponse(blog, currentUsername))
                .collect(Collectors.toList());
    }

    public BlogResponse updateBlog(Long id, BlogRequest request, UserDetails userDetails) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));

        if (!blog.getAuthor().getUsername().equals(userDetails.getUsername())) {
            throw new RuntimeException("You are not authorized to update this blog");
        }

        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        
        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            try {
                if (blog.getImageUrl() != null) {
                    fileStorageService.deleteBlogImage(blog.getImageUrl());
                }
                blog.setImageUrl(fileStorageService.storeBlogImage(request.getImageFile()));
            } catch (RuntimeException e) {
                throw new RuntimeException("Failed to update image: " + e.getMessage());
            }
        }
        
        Blog updatedBlog = blogRepository.save(blog);
        return mapToBlogResponse(updatedBlog, userDetails.getUsername());
    }

    public void deleteBlog(Long id, UserDetails userDetails) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        
        // Check if the user is the author of the blog or has ADMIN role
        boolean isAdmin = userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isAuthor = blog.getAuthor().getUsername().equals(userDetails.getUsername());
        
        if (!isAdmin && !isAuthor) {
            throw new RuntimeException("You are not authorized to delete this blog");
        }
        
        if (blog.getImageUrl() != null) {
            fileStorageService.deleteBlogImage(blog.getImageUrl());
        }

        blogRepository.delete(blog);
    }

    public BlogResponse mapToBlogResponse(Blog blog, String currentUsername) {
//        boolean isLiked = currentUsername != null && 
//            likeService.hasUserLikedBlog(blog.getId(), currentUsername);
        
    	boolean isLiked = false;
        boolean isSaved = false;
        
        if (currentUsername != null) {
            User currentUser = userRepository.findByUsername(currentUsername).orElse(null);
            if (currentUser != null) {
                isLiked = likeService.hasUserLikedBlog(blog.getId(), currentUsername);
                // Check saved status using injected SavedBlogRepository
                isSaved = savedBlogRepository.existsByUserAndBlog(currentUser, blog);
            }
        }
    	
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .topic(blog.getTopic())
                .topicDisplayName(blog.getTopic().getDisplayName())
                .imageUrl(blog.getImageUrl())
                .createdAt(blog.getCreatedAt())
                .authorId(blog.getAuthor().getId())
                .authorName(blog.getAuthor().getName())
                .authorUsername(blog.getAuthor().getUsername())
                .authorProfilePictureUrl(blog.getAuthor().getProfilePictureUrl())
                .authorRole(blog.getAuthor().getRole())
                .likeCount(likeService.getLikeCount(blog.getId()))
                .isLikedByCurrentUser(isLiked)
                .isSavedByCurrentUser(isSaved)
                .commentCount(commentService.getCommentCount(blog.getId()))
                .build();
    }
    
    public List<BlogResponse> getBlogsByTopic(Topic topic, String currentUsername) {
        return blogRepository.findByTopic(topic).stream()
                .map(blog -> mapToBlogResponse(blog, currentUsername))
                .collect(Collectors.toList());
    }
    
    public List<BlogResponse> getBlogsFromFollowedUsers(String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<User> followingUsers = followService.getFollowing(currentUser.getId());

        return followingUsers.stream()
                .flatMap(followedUser -> blogRepository.findByAuthor(followedUser).stream())
                .sorted(Comparator.comparing(Blog::getCreatedAt).reversed())
                .map(blog -> mapToBlogResponse(blog, currentUsername))
                .collect(Collectors.toList());
    }

    public List<BlogResponse> getTopLikedBlogsOfWeek(String currentUsername) {
    	LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return blogRepository.findTopBlogsByLikes(sevenDaysAgo, org.springframework.data.domain.PageRequest.of(0, 3)).stream()
                .map(blog -> mapToBlogResponse(blog, currentUsername))
                .collect(Collectors.toList());
    }
  
    public List<BlogResponse> getRecentlyVisitedBlogs(Long userId, String currentUsername) {
        return visitedBlogService.getRecentlyVisitedBlogs(userId).stream()
                .limit(3)
                .map(visitedBlog -> mapToBlogResponse(visitedBlog.getBlog(), currentUsername))
                .collect(Collectors.toList());
    }
}