package com.FittPeeps.ai_service.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Service
public class GeminiService {
    private final RestClient restClient;
    @Value("${gemini.api.url}")
    private String url;
    @Value("${gemini.api.key}")
    private String key;

    public GeminiService(RestClient.Builder restClientBuilder) {
        this.restClient= restClientBuilder.build();
//        this.restClient = restClient;
    }

    public String getAnswer(String prompt){
        Map<String ,Object> requestBody = Map.of(
                "contents",new Object[]{
                        Map.of("parts",new Object[]{
                                Map.of("text",prompt)
                        })
                }
        );
        String geminiResponse=
                restClient.post()
                .uri(url+key)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(String.class);
        return geminiResponse;
    }
}
