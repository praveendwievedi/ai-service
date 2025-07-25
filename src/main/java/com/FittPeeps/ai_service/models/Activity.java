package com.FittPeeps.ai_service.models;

import java.time.LocalDateTime;

public record Activity(String id, String userId, String activityType, LocalDateTime startTime,Integer duration, Integer caloriesBurned, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
