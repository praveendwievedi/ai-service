package com.FittPeeps.ai_service.services;

import com.FittPeeps.ai_service.models.Activity;
import com.FittPeeps.ai_service.models.Recommendation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityAiService {
    private  final  GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
//        log.info("RESPONSE FROM AI: {} ", aiResponse);
        return processAiResponse(activity, aiResponse);
//        return processAiResponse(activity, aiResponse);
    }

    public Recommendation processAiResponse(Activity activity, String aiResponse){
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(aiResponse);

            String neededResponse=jsonNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
            String textResponse= neededResponse.replaceAll("```json\\n","")
                    .replaceAll("\\n```", "")
                    .trim();
            log.info("Processed AI response: {}", textResponse);
            JsonNode textNode=mapper.readTree(textResponse);
            StringBuilder fullAnalysis= new StringBuilder();
            addAnalysisToString(fullAnalysis, textNode.path("analysis"), "overall","Overall : ");
            addAnalysisToString(fullAnalysis, textNode.path("analysis"), "pace","Pace : ");
            addAnalysisToString(fullAnalysis, textNode.path("analysis"), "hearRate","Heart-Rate : ");
            addAnalysisToString(fullAnalysis, textNode.path("analysis"), "caloriesBurned","Calories : ");

            List<String> improvements = extractImprovements(textNode.path("improvements"));
            List<String> suggestions = extractSuggestions(textNode.path("suggestions"));
            List<String> safetyPoints = extractSafetyPoints(textNode.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.id())
                    .userId(activity.userId())
                    .recommendation(fullAnalysis.toString())
                    .activityType(activity.activityType())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safetyTips(safetyPoints)
                    .createdAt(LocalDateTime.now())
                    .build();
//            JsonNde analysisNode=mapper.readTree(neededResponse);
        }
        catch (Exception e) {
            log.error("Error processing AI response: {}", e.getMessage());
            return Recommendation.builder()
                .activityId(activity.id())
                .userId(activity.userId())
                .recommendation("No recommendation found")
                .activityType(activity.activityType())
                .improvements(Collections.singletonList("No improvements found"))
                .suggestions(Collections.singletonList("No suggestions found"))
                .safetyTips(Collections.singletonList("Follow general safety guidelines"))
                .createdAt(LocalDateTime.now())
                .build();
        }
    }
    private List<String> extractSafetyPoints(JsonNode safetyNode) {
        List<String> safetyPointList= new ArrayList<>();
        if(safetyNode.isArray()){
            safetyNode.forEach(points -> {
                safetyPointList.add(points.asText());
            });
        }
        return safetyPointList.isEmpty() ? Collections.singletonList("Follow general safety guidelines") : safetyPointList;
    }
    private List<String> extractSuggestions(JsonNode suggestions) {
        List<String> suggestionList= new ArrayList<>();
        if(suggestions.isArray()){
            suggestions.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestionList.add(String.format("Workout: %s, Description: %s", workout, description));
            });
        }
        return suggestionList.isEmpty() ? Collections.singletonList("No suggestions You are doing good") : suggestionList;
    }

    private List<String> extractImprovements(JsonNode improvements) {
        List<String> improvementList= new ArrayList<>();
        if(improvements.isArray()){
            improvements.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String recommendation = improvement.path("recommendation").asText();
                improvementList.add(String.format("Area: %s, Recommendation: %s", area, recommendation));
            });
        }
        return improvementList.isEmpty() ? Collections.singletonList("No improvements found") : improvementList;
    }

    private void addAnalysisToString(StringBuilder stringBuilder, JsonNode analysisNode, String key, String prefix) {
//        JsonNode node=analysisNode.path(key);
        if(!analysisNode.path(key).isMissingNode()){
            stringBuilder.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
        {
          "analysis": {
            "overall": "Overall analysis here",
            "pace": "Pace analysis here",
            "heartRate": "Heart rate analysis here",
            "caloriesBurned": "Calories analysis here"
          },
          "improvements": [
            {
              "area": "Area name",
              "recommendation": "Detailed recommendation"
            }
          ],
          "suggestions": [
            {
              "workout": "Workout name",
              "description": "Detailed workout description"
            }
          ],"safety": [
        "Safety point 1",
                "Safety point 2"
          ]
    }

    Analyze this activity:
    Activity Type: %s
    Duration: %d minutes
    Calories Burned: %d
    Additional: %s

    Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
    Ensure the response follows the EXACT JSON format shown above.
            """,
                activity.activityType(),
                activity.duration(),
                activity.caloriesBurned(),
                activity.additional()
//                activity.
        );
    }

}
