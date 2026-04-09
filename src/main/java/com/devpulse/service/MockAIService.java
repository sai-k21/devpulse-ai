package com.devpulse.service;

import com.devpulse.model.RepoAnalysis;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service("mockAIService")
public class MockAIService {

    public RepoAnalysis analyzeRepository(String repoUrl, String commits, String issues) {
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
                .analyzedAt(LocalDateTime.now().toString())
                .build();
    }
}