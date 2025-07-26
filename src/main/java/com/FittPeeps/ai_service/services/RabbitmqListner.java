package com.FittPeeps.ai_service.services;

import com.FittPeeps.ai_service.models.Activity;
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

    @Value("${rabbitmq.queue.name}")
    private String queueName;



    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void listenToRabbitMq(Activity activity){
        log.info("Received activity from RabbitMQ: {}", activity);
        activityAiService.generateRecommendation(activity);
//        return activity;
    }
}
