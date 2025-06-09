package com.blogsphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogsphere.model.Follow;
import com.blogsphere.model.User;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowing(User following);
    boolean existsByFollowerAndFollowing(User follower, User following);
    long countByFollowing(User following);
    long countByFollower(User follower);
}