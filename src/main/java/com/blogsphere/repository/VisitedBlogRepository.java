package com.blogsphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogsphere.model.Blog;
import com.blogsphere.model.User;
import com.blogsphere.model.VisitedBlog;

@Repository
public interface VisitedBlogRepository extends JpaRepository<VisitedBlog, Long> {
    Optional<VisitedBlog> findByUserAndBlog(User user, Blog blog);
    List<VisitedBlog> findByUserOrderByVisitedAtDesc(User user);
}