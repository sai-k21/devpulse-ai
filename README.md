# DevPulse AI

AI-powered developer productivity API that automatically fetches real GitHub repository data and uses Google Gemini to generate standup summaries, code health scores, risk assessments, and recommended actions.

## How It Works

1. You pass any public GitHub repository URL
2. DevPulse AI automatically fetches the last 10 commits and open issues via GitHub API
3. Sends real repository data to Google Gemini with structured prompts
4. Returns AI-generated intelligence as structured JSON

No manual input needed — just a repo URL.

## Features

- Auto-fetches commits and issues from any public GitHub repository
- AI-generated daily standup summaries from real commit history
- Code health score with explanation
- Risk assessment from open issues and commit patterns
- Recommended actions for the engineering team
- Mock mode for development without API quota concerns
- Spring Boot Actuator health and metrics endpoints
- Swagger/OpenAPI 3 documentation
- Multi-stage Dockerfile for production builds
- GitHub Actions CI/CD pipeline — tests and Docker build on every push

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5 |
| AI | Google Gemini API (gemini-2.0-flash-lite) |
| Data Source | GitHub REST API (public repos, no auth needed) |
| Containerization | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| API Docs | Swagger UI / OpenAPI 3 |
| Observability | Spring Boot Actuator |

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | /api/v1/health | Health check |
| POST | /api/v1/analyze | Analyze any public GitHub repo automatically |
| GET | /api/v1/analyze/demo | Demo using cloudtask-api repo |

## Running Locally

### Prerequisites

- Java 17+
- Maven 3.9+
- Gemini API key from aistudio.google.com (optional — mock mode works without it)

### Run in mock mode (no API key needed)

```bash
$env:GEMINI_API_KEY="dummy"; mvn spring-boot:run
```

### Run with real Gemini AI

```bash
$env:GEMINI_API_KEY="your_real_key_here"; mvn spring-boot:run
```

Visit: http://localhost:8080/swagger-ui.html

### Switch between mock and real AI

Set environment variable before running:

```bash
$env:AI_PROVIDER="mock"; $env:GEMINI_API_KEY="dummy"; mvn spring-boot:run
$env:AI_PROVIDER="gemini"; $env:GEMINI_API_KEY="your_key"; mvn spring-boot:run
```

## Example Request

```bash
curl -X POST "http://localhost:8080/api/v1/analyze?repoUrl=https://github.com/sai-k21/cloudtask-api"
```

## Example Response

```json
{
  "success": true,
  "message": "Repository analyzed successfully",
  "data": {
    "repositoryUrl": "https://github.com/sai-k21/cloudtask-api",
    "standupSummary": "Team merged 4 PRs fixing auth timeout and DB performance issues.",
    "codeHealthScore": "7.5/10 - Good test coverage, two critical issues need attention.",
    "riskAssessment": "Two HIGH priority issues open 14+ days - potential release blockers.",
    "topIssues": [
      "DB connection timeout recurring in production",
      "Auth token expiry not handled gracefully",
      "Memory leak detected in background worker service"
    ],
    "recommendedActions": "Prioritise DB connection pool fix before next release."
  }
}
```

## Architecture

```
Client Request (repo URL)
  └── DevPulseController
        ├── GitHubService → fetches real commits and issues from GitHub API
        └── GeminiService → sends data to Gemini, returns AI analysis
              ├── Mock mode  → instant hardcoded responses (development)
              └── Gemini mode → real AI analysis (production)
```

## Author

**Sai Kumar Moguluri**

- LinkedIn: https://linkedin.com/in/sai-1899k
- GitHub: https://github.com/sai-k21