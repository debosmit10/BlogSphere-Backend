package com.blogsphere.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blogsphere.dto.DashboardStatsDTO;
import com.blogsphere.dto.UserResponseDTO;
import com.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.model.Role;
import com.blogsphere.model.User;
import com.blogsphere.repository.BlogRepository;
import com.blogsphere.repository.CommentRepository;
import com.blogsphere.repository.UserRepository;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentRepository commentRepository;

    public DashboardStatsDTO getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalPosts = blogRepository.count();
        long totalComments = commentRepository.count();

        LocalDateTime now = LocalDateTime.now();

        // Posts Today
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        long postsToday = blogRepository.countByCreatedAtAfter(startOfDay);

        // Posts This Week (Monday to Sunday)
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        long postsThisWeek = blogRepository.countByCreatedAtAfter(startOfWeek);

        // Posts This Month
        LocalDateTime startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
        long postsThisMonth = blogRepository.countByCreatedAtAfter(startOfMonth);

        // Posts This Year
        LocalDateTime startOfYear = now.with(TemporalAdjusters.firstDayOfYear()).toLocalDate().atStartOfDay();
        long postsThisYear = blogRepository.countByCreatedAtAfter(startOfYear);

        return DashboardStatsDTO.builder()
                .totalUsers(totalUsers)
                .totalPosts(totalPosts)
                .totalComments(totalComments)
                .postsToday(postsToday)
                .postsThisWeek(postsThisWeek)
                .postsThisMonth(postsThisMonth)
                .postsThisYear(postsThisYear)
                .build();
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponseDTO)
                .collect(Collectors.toList());
    }
    
    private UserResponseDTO mapToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        userRepository.delete(user);
    }

    @Transactional
    public User updateUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        user.setRole(Role.valueOf(newRole));
        return userRepository.save(user);
    }
}