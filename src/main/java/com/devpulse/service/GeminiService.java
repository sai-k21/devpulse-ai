package com.devpulse.service;

import com.devpulse.config.GeminiConfig;
import com.devpulse.model.RepoAnalysis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    @Value("${ai.provider:gemini}")
    private String aiProvider;

    private final RestTemplate restTemplate;
    private final GeminiConfig geminiConfig;

    public RepoAnalysis analyzeRepository(String repoUrl, String commits, String issues) {
        if ("mock".equals(aiProvider)) {
            return RepoAnalysis.builder()
                    .repositoryUrl(repoUrl)
                    .standupSummary("Team merged 4 PRs yesterday — fixed authentication timeout, improved database query performance, and resolved CI pipeline failures. Active development on payment module with 3 open PRs under review.")
                    .codeHealthScore("7.5/10 — Good test coverage and clean architecture. Two critical issues unresolved for 14+ days need immediate attention before next release.")
                    .riskAssessment("Two HIGH priority issues open for 14+ days indicate potential release blockers. Database connection timeout appearing in multiple commits suggests underlying infrastructure issue not fully resolved.")
                    .topIssues(List.of(
                            "Database connection timeout recurring in production",
                            "Authentication token expiry not handled gracefully",
                            "Memory leak detected in background worker service"
                    ))
                    .recommendedActions("Prioritise database connection pool configuration fix. Schedule auth token refresh mechanism for current sprint. Profile memory usage in worker service before next deployment.")
                    .analyzedAt(java.time.LocalDateTime.now().toString())
                    .build();
        }
        String prompt = buildPrompt(repoUrl, commits, issues);
        String geminiResponse = callGemini(prompt);
        return parseResponse(repoUrl, geminiResponse);
    }

    private String buildPrompt(String repoUrl, String commits, String issues) {
        return String.format("""
            You are an expert software engineering analyst. Analyze this GitHub repository data and respond in JSON format only.
            
            Repository: %s
            Recent Commits: %s
            Open Issues: %s
            
            Respond with ONLY this JSON structure, no markdown:
            {
              "standupSummary": "2-3 sentence daily standup summary of recent activity",
              "codeHealthScore": "score from 1-10 with one line explanation",
              "riskAssessment": "key risks identified from issues and commits",
              "topIssues": ["issue 1", "issue 2", "issue 3"],
              "recommendedActions": "top 2-3 recommended next actions for the team"
            }
            """, repoUrl, commits, issues);
    }

    @SuppressWarnings("unchecked")
    private String callGemini(String prompt) {
        String url = geminiConfig.getApiUrl() + "?key=" + geminiConfig.getApiKey();

        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> body = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                (Class<Map<String, Object>>) (Class<?>) Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null) return fallbackJson();

            List<Map<String, Object>> candidates =
                (List<Map<String, Object>>) responseBody.get("candidates");
            Map<String, Object> candidate = candidates.get(0);
            Map<String, Object> candidateContent =
                (Map<String, Object>) candidate.get("content");
            List<Map<String, Object>> parts =
                (List<Map<String, Object>>) candidateContent.get("parts");
            return parts.get(0).get("text").toString();

        } catch (Exception e) {
            log.error("Gemini API call failed: {}", e.getMessage());
            return fallbackJson();
        }
    }

    private String fallbackJson() {
        return """
            {
              "standupSummary": "Analysis unavailable",
              "codeHealthScore": "N/A",
              "riskAssessment": "N/A",
              "topIssues": [],
              "recommendedActions": "N/A"
            }
            """;
    }

    private RepoAnalysis parseResponse(String repoUrl, String json) {
        try {
            json = json.trim()
                      .replace("```json", "")
                      .replace("```", "")
                      .trim();

            return RepoAnalysis.builder()
                    .repositoryUrl(repoUrl)
                    .standupSummary(extractField(json, "standupSummary"))
                    .codeHealthScore(extractField(json, "codeHealthScore"))
                    .riskAssessment(extractField(json, "riskAssessment"))
                    .topIssues(extractList(json, "topIssues"))
                    .recommendedActions(extractField(json, "recommendedActions"))
                    .analyzedAt(LocalDateTime.now().toString())
                    .build();
        } catch (Exception e) {
            log.error("Parse failed: {}", e.getMessage());
            return RepoAnalysis.builder()
                    .repositoryUrl(repoUrl)
                    .standupSummary("Could not parse response")
                    .analyzedAt(LocalDateTime.now().toString())
                    .build();
        }
    }

    private String extractField(String json, String field) {
        try {
            String key = "\"" + field + "\"";
            int keyIndex = json.indexOf(key);
            if (keyIndex == -1) return "N/A";
            int colonIndex = json.indexOf(":", keyIndex);
            int valueStart = json.indexOf("\"", colonIndex) + 1;
            int valueEnd = json.indexOf("\"", valueStart);
            return json.substring(valueStart, valueEnd);
        } catch (Exception e) {
            return "N/A";
        }
    }

    private List<String> extractList(String json, String field) {
        try {
            String key = "\"" + field + "\"";
            int keyIndex = json.indexOf(key);
            int arrStart = json.indexOf("[", keyIndex) + 1;
            int arrEnd = json.indexOf("]", arrStart);
            String arr = json.substring(arrStart, arrEnd);
            List<String> result = new ArrayList<>();
            for (String item : arr.split(",")) {
                String cleaned = item.trim().replace("\"", "");
                if (!cleaned.isEmpty()) result.add(cleaned);
            }
            return result;
        } catch (Exception e) {
            return List.of();
        }
    }
}