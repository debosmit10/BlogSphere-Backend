package com.blogsphere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogsphere.model.Blog;
import com.blogsphere.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBlogIdOrderByCreatedAtAsc(Long blogId);
    int countByBlog(Blog blog);
}