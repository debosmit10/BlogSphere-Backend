package com.blogsphere.repository;

import com.blogsphere.model.SavedBlog;
import com.blogsphere.model.User;
import com.blogsphere.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedBlogRepository extends JpaRepository<SavedBlog, Long> {
    
    /**
     * Find all saved blogs for a specific user, ordered by saved date descending.
     */
    List<SavedBlog> findByUserOrderBySavedAtDesc(User user);
    
    /**
     * Check if a blog is saved by a specific user.
     */
    boolean existsByUserAndBlog(User user, Blog blog);
    
    /**
     * Find a saved blog entry by user and blog.
     */
    Optional<SavedBlog> findByUserAndBlog(User user, Blog blog);
    
    /**
     * Delete a saved blog entry by user and blog.
     */
    void deleteByUserAndBlog(User user, Blog blog);
}
