package com.FittPeeps.ai_service.controllers;

import com.FittPeeps.ai_service.models.Recommendation;
//import com.FittPeeps.ai_service.services.RecomendationService;
import com.FittPeeps.ai_service.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
private  final RecommendationService recommendationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getUserRecommendations(@PathVariable String userId) {
        List<Recommendation> recommendations= recommendationService.getRecommendationsByUserId(userId);
        return ResponseEntity.ok(recommendations);
    }


    @GetMapping("/activity/{activityId}")
    public ResponseEntity<?> getActivityRecommendation(@PathVariable String activityId) {
        Recommendation recomendation= recommendationService.getActivityRecommendation(activityId);
        if(recomendation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO Recommendation found for this activity : " + activityId);
        }
        return ResponseEntity.ok(recomendation);
    }

    @GetMapping("/test")
    public String testConnection() {
        return "AI Service is live";
    }
}
