package com.FittPeeps.ai_service.models;

import java.time.LocalDateTime;
import java.util.Map;

public record Activity(String id, String userId, String activityType, LocalDateTime startTime, Integer duration, Integer caloriesBurned, LocalDateTime createdAt, LocalDateTime updatedAt, Map<String,Object> additional) {
}
