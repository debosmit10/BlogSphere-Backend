package com.blogsphere.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "blogs")

public class Blog {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "image_url")
    private String imageUrl;
	
	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Topic topic;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private User author;

	@OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();
	
	// Helper method to get like count
    public int getLikeCount() {
        return likes.size();
    }
    
    // Helper method to check if user liked the blog
    public boolean isLikedByUser(User user) {
        return likes.stream().anyMatch(like -> like.getUser().equals(user));
    }
    
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    
    public int getCommentCount() {
    	return comments.size();
    }
}
