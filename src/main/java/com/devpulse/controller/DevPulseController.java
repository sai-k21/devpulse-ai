package com.devpulse.controller;

import com.devpulse.model.ApiResponse;
import com.devpulse.model.RepoAnalysis;
import com.devpulse.service.GeminiService;
import com.devpulse.service.GitHubService;
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
    private final GitHubService gitHubService;

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("DevPulse AI is running", "OK"));
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyze a GitHub repository with AI - auto fetches commits and issues")
    public ResponseEntity<ApiResponse<RepoAnalysis>> analyzeRepo(
            @RequestParam String repoUrl) {

        log.info("Analyzing repository: {}", repoUrl);

        String[] ownerRepo = gitHubService.parseRepoUrl(repoUrl);
        String commits = gitHubService.fetchRecentCommits(ownerRepo[0], ownerRepo[1]);
        String issues = gitHubService.fetchOpenIssues(ownerRepo[0], ownerRepo[1]);

        log.info("Fetched {} chars of commits and {} chars of issues", 
            commits.length(), issues.length());

        RepoAnalysis analysis = geminiService.analyzeRepository(repoUrl, commits, issues);
        return ResponseEntity.ok(ApiResponse.success(analysis, "Repository analyzed successfully"));
    }

    @GetMapping("/analyze/demo")
    @Operation(summary = "Demo - auto fetches real data from CloudTask API repo")
    public ResponseEntity<ApiResponse<RepoAnalysis>> analyzeDemo() {
        String demoRepo = "https://github.com/sai-k21/cloudtask-api";

        String[] ownerRepo = gitHubService.parseRepoUrl(demoRepo);
        String commits = gitHubService.fetchRecentCommits(ownerRepo[0], ownerRepo[1]);
        String issues = gitHubService.fetchOpenIssues(ownerRepo[0], ownerRepo[1]);

        RepoAnalysis analysis = geminiService.analyzeRepository(demoRepo, commits, issues);
        return ResponseEntity.ok(ApiResponse.success(analysis, "Demo analysis completed"));
    }
}