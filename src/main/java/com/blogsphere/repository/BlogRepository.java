package com.blogsphere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogsphere.model.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
	List<Blog> findByAuthorId(Long authorId);
}
