package com.FittPeeps.ai_service.repository;

import com.FittPeeps.ai_service.models.Recomendation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends MongoRepository<Recomendation,String > {
    List<Recomendation> findByUserId(String userId);

    Optional<Recomendation> findByActivityId(String activityId);
}
