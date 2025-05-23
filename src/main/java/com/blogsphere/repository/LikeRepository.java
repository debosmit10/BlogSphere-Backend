package com.blogsphere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogsphere.model.Blog;
import com.blogsphere.model.Like;
import com.blogsphere.model.User;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndBlog(User user, Blog blog);
    boolean existsByUserAndBlog(User user, Blog blog);
    int countByBlog(Blog blog);
}