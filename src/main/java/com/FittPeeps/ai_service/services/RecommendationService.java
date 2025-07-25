package com.FittPeeps.ai_service.services;

import com.FittPeeps.ai_service.models.Recomendation;
import com.FittPeeps.ai_service.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private  final RecommendationRepository recommendationRepository;

    public List<Recomendation> getRecommendationsByUserId(String userId) {
        List<Recomendation> recomendations=recommendationRepository.findByUserId(userId);
        return recomendations.isEmpty() ? Collections.emptyList() : recomendations;
    }

    public Recomendation getActivityRecommendation(String activityId) {
        Recomendation recomendation= recommendationRepository.findByActivityId(activityId)
                .orElse(null);
        return recomendation;
    }
}
