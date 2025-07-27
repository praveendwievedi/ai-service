package com.FittPeeps.ai_service.services;

import com.FittPeeps.ai_service.models.Activity;
import com.FittPeeps.ai_service.models.Recommendation;
import com.FittPeeps.ai_service.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitmqListner {

    private final ActivityAiService activityAiService;
    private final RecommendationRepository recommendationRepository;

    @Value("${rabbitmq.queue.name}")
    private String queueName;



    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void listenToRabbitMq(Activity activity){
        log.info("Received activity from RabbitMQ: {}", activity.id());
        Recommendation recommendation= activityAiService.generateRecommendation(activity);
        recommendationRepository.save(recommendation);
//        return activity;
    }
}
