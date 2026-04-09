package com.devpulse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
@Slf4j
public class GitHubService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchRecentCommits(String owner, String repo) {
        try {
            String url = "https://api.github.com/repos/" + owner + "/" + repo + "/commits?per_page=10";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/vnd.github.v3+json");
            headers.set("User-Agent", "DevPulse-AI");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, List.class
            );

            if (response.getBody() == null) return "No commits found";

            StringBuilder commits = new StringBuilder();
            List<Map<String, Object>> commitList = (List<Map<String, Object>>) response.getBody();
            for (Map<String, Object> item : commitList) {
                Map<String, Object> commit = (Map<String, Object>) item.get("commit");
                String message = commit.get("message").toString().split("\n")[0];
                commits.append("- ").append(message).append("\n");
            }
            return commits.toString();
        } catch (Exception e) {
            log.error("Failed to fetch commits: {}", e.getMessage());
            return "Could not fetch commits from GitHub";
        }
    }

    public String fetchOpenIssues(String owner, String repo) {
        try {
            String url = "https://api.github.com/repos/" + owner + "/" + repo + "/issues?state=open&per_page=10";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/vnd.github.v3+json");
            headers.set("User-Agent", "DevPulse-AI");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, List.class
            );

            if (response.getBody() == null) return "No open issues";

            StringBuilder issues = new StringBuilder();
            List<Map<String, Object>> issueList = (List<Map<String, Object>>) response.getBody();
            for (Map<String, Object> issue : issueList) {
                String title = issue.get("title").toString();
                issues.append("- ").append(title).append("\n");
            }
            return issues.isEmpty() ? "No open issues" : issues.toString();
        } catch (Exception e) {
            log.error("Failed to fetch issues: {}", e.getMessage());
            return "Could not fetch issues from GitHub";
        }
    }

    public String[] parseRepoUrl(String repoUrl) {
        // Handle formats: github.com/owner/repo or owner/repo
        repoUrl = repoUrl.replace("https://", "").replace("http://", "")
                        .replace("github.com/", "").trim();
        String[] parts = repoUrl.split("/");
        if (parts.length >= 2) {
            return new String[]{parts[0], parts[1]};
        }
        return new String[]{"unknown", "unknown"};
    }
}