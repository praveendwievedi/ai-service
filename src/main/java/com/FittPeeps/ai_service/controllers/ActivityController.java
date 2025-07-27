package com.FittPeeps.ai_service.controllers;

import com.FittPeeps.ai_service.services.ActivityAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/check/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityAiService activityAiService;

//    @PostMapping("/generate-recomendation")
}
