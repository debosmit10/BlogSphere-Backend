package com.blogsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalUsers;
    private long totalPosts;
    private long totalComments;
    private long postsToday;
    private long postsThisWeek;
    private long postsThisMonth;
    private long postsThisYear;
}