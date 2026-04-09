package com.devpulse.controller;

import com.devpulse.model.ApiResponse;
import com.devpulse.model.RepoAnalysis;
import com.devpulse.service.GeminiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "DevPulse AI", description = "AI-powered repository analysis using Google Gemini")
@CrossOrigin(origins = "*")
public class DevPulseController {

    private final GeminiService geminiService;

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("DevPulse AI is running", "OK"));
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyze a GitHub repository with AI")
    public ResponseEntity<ApiResponse<RepoAnalysis>> analyzeRepo(
            @RequestParam String repoUrl,
            @RequestParam(defaultValue = "No commit data provided") String commits,
            @RequestParam(defaultValue = "No issues provided") String issues) {

        log.info("Analyzing repository: {}", repoUrl);
        RepoAnalysis analysis = geminiService.analyzeRepository(repoUrl, commits, issues);
        return ResponseEntity.ok(ApiResponse.success(analysis, "Repository analyzed successfully"));
    }

    @GetMapping("/analyze/demo")
    @Operation(summary = "Demo analysis with sample data")
    public ResponseEntity<ApiResponse<RepoAnalysis>> analyzeDemo() {
        String demoUrl = "https://github.com/sai-k21/cloudtask-api";
        String demoCommits = "Fixed task status update bug, Added overdue task detection, Improved CI/CD pipeline";
        String demoIssues = "Pagination not implemented, Authentication needed, Performance optimization for large datasets";

        RepoAnalysis analysis = geminiService.analyzeRepository(demoUrl, demoCommits, demoIssues);
        return ResponseEntity.ok(ApiResponse.success(analysis, "Demo analysis completed"));
    }
}