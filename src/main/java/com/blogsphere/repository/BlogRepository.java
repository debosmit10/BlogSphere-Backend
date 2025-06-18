package com.blogsphere.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blogsphere.model.Blog;
import com.blogsphere.model.Topic;
import com.blogsphere.model.User;

public interface BlogRepository extends JpaRepository<Blog, Long> {
	List<Blog> findByAuthorId(Long authorId);
	List<Blog> findByTopic(Topic topic);
	List<Blog> findByAuthor(User author);
	List<Blog> findByAuthorOrderByCreatedAtDesc(User author);
	long countByCreatedAtAfter(java.time.LocalDateTime dateTime);
	
	@Query("SELECT b FROM Blog b LEFT JOIN b.likes l WHERE b.createdAt >= :startDate GROUP BY b ORDER BY COUNT(l) DESC")
    List<Blog> findTopBlogsByLikes(@Param("startDate") LocalDateTime startDate, Pageable pageable);
}
