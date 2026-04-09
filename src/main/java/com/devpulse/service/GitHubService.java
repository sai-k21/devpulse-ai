package com.devpulse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

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
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getBody() == null) return "No commits found";

            StringBuilder commits = new StringBuilder();
            for (Map<String, Object> item : response.getBody()) {
                Map<?, ?> commit = (Map<?, ?>) item.get("commit");
                if (commit != null) {
                    String message = commit.get("message").toString().split("\n")[0];
                    commits.append("- ").append(message).append("\n");
                }
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
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getBody() == null) return "No open issues";

            StringBuilder issues = new StringBuilder();
            for (Map<String, Object> issue : response.getBody()) {
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
        repoUrl = repoUrl.replace("https://", "").replace("http://", "")
                        .replace("github.com/", "").trim();
        String[] parts = repoUrl.split("/");
        if (parts.length >= 2) {
            return new String[]{parts[0], parts[1]};
        }
        return new String[]{"unknown", "unknown"};
    }
}