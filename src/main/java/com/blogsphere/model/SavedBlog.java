package com.blogsphere.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "saved_blogs", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "blog_id"}))
public class SavedBlog {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;
    
    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt;
}
