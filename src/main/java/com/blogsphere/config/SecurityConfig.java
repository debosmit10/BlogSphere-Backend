package com.blogsphere.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	// Disable CSRF for stateless API
            .csrf(AbstractHttpConfigurer::disable)
            
            // Enable CORS with default configuration
            .cors(Customizer.withDefaults())
            
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
            	// Public endpoints
                .requestMatchers(
                		"/api/auth/**",          // Authentication endpoints
                        "/api/blogs",            // Get all blogs
                        "/api/blogs/{id}",       // Get single blog
                        "/api/blogs/user/**",     // Get user's blogs
                        "/api/blogs/topic/**",   // Get specific topic blogs
                        "/api/blogs/topics",	 // Get all topics
                        "/api/files/**",         // File access endpoints
                        "/api/users/*/profile",   // User profile access
                        "/api/comments/blogs/**", // Get comments by blog ID
                        "/api/comment-likes/*/count", // Get comment like count
                        "/api/comment-likes/*/status/users/**", // Get comment like status
                        "/api/follows/**" // Follow endpoints
                ).permitAll()
                
                // Authenticated endpoints
                .requestMatchers(
                		"/api/blogs/my-blogs",   // User's blogs
                		//"/api/blogs/**",         // All other blog operations
                		"/api/blogs/*/likes/**",
                		"/api/comments/blogs/*/users/**", // Create comment
                        "/api/comments/*/users/**", // Delete comment
                        "/api/comment-likes/*/users/**", // Toggle comment like
                        "/api/follows/{followerId}/following/{followingId}" // Toggle follow
                ).authenticated()
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            
            // Set session management to stateless
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Add JWT filter before the username/password filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
	
		// CORS configuration
		@Bean
		public CorsConfigurationSource corsConfigurationSource() {
		    CorsConfiguration configuration = new CorsConfiguration();
		    configuration.setAllowedOrigins(List.of("http://localhost:5173"));	// Allowed origins
		    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));	// Allowed HTTP methods
		    configuration.setAllowedHeaders(List.of("*"));		// Allowed headers
		    configuration.setExposedHeaders(List.of("Authorization"));
		    configuration.setAllowCredentials(true);	// Allow credentials (for cookies/auth headers)

		    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		    // Apply CORS config to all paths
		    source.registerCorsConfiguration("/**", configuration);
		    return source;
		}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
