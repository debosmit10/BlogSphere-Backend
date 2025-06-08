package com.blogsphere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogsphere.model.Blog;
import com.blogsphere.model.Topic;
import com.blogsphere.model.User;

public interface BlogRepository extends JpaRepository<Blog, Long> {
	List<Blog> findByAuthorId(Long authorId);
	List<Blog> findByTopic(Topic topic);
	List<Blog> findByAuthor(User author);
}
