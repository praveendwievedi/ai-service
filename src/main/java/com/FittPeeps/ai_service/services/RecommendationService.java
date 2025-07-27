package com.FittPeeps.ai_service.services;

import com.FittPeeps.ai_service.models.Recommendation;
import com.FittPeeps.ai_service.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private  final RecommendationRepository recommendationRepository;

    public List<Recommendation> getRecommendationsByUserId(String userId) {
        List<Recommendation> recommendations=recommendationRepository.findByUserId(userId);
        return recommendations.isEmpty() ? Collections.emptyList() : recommendations;
    }

    public Recommendation getActivityRecommendation(String activityId) {
        Recommendation recommendation= recommendationRepository.findByActivityId(activityId)
                .orElse(null);
        return recommendation;
    }
}
