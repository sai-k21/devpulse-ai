package com.devpulse.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class RepoAnalysis {
    private String repositoryUrl;
    private String standupSummary;
    private String codeHealthScore;
    private String riskAssessment;
    private List<String> topIssues;
    private String recommendedActions;
    private String analyzedAt;
}