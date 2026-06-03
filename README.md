# DevPulse AI

AI-powered developer productivity API that fetches real GitHub repository data and uses Google Gemini to generate standup summaries, code health scores, risk assessments, and recommended actions.

## How It Works

1. Pass any public GitHub repository URL
2. DevPulse fetches the last 10 commits and open issues via GitHub API
3. Sends real repository data to Google Gemini with structured prompts
4. Returns AI-generated intelligence as structured JSON

No manual input needed — just a repo URL.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5 |
| AI | Google Gemini API (gemini-2.0-flash-lite) |
| Data Source | GitHub REST API |
| Containerization | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| API Docs | Swagger UI / OpenAPI 3 |
| Observability | Spring Boot Actuator |

## Features

- Auto-fetches commits and issues from any public GitHub repository
- AI-generated daily standup summaries from real commit history
- Code health score with explanation
- Risk assessment from open issues and commit patterns
- Recommended actions for the engineering team
- Mock mode for development without API quota usage

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | /api/v1/health | Health check |
| POST | /api/v1/analyze | Analyze any public GitHub repo |
| GET | /api/v1/analyze/demo | Demo using cloudtask-api repo |

## Architecture

```
Client Request (repo URL)
  └── DevPulseController
        ├── GitHubService  — fetches commits and issues from GitHub API
        └── GeminiService  — sends data to Gemini, returns AI analysis
              ├── Mock mode   — instant responses for development
              └── Gemini mode — real AI analysis for production
```

## Getting Started

**Prerequisites:** Java 17+, Maven 3.9+

```bash
git clone https://github.com/sai-k21/devpulse-ai.git
cd devpulse-ai
```

Run in mock mode (no API key needed):

```bash
# Mac/Linux
GEMINI_API_KEY=dummy mvn spring-boot:run

# Windows
$env:GEMINI_API_KEY="dummy"; mvn spring-boot:run
```

Run with real Gemini AI:

```bash
# Mac/Linux
GEMINI_API_KEY=your_key_here mvn spring-boot:run

# Windows
$env:GEMINI_API_KEY="your_key_here"; mvn spring-boot:run
```

Visit: `http://localhost:8080/swagger-ui.html`

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
    "codeHealthScore": "7.5/10 — Good test coverage, two critical issues need attention.",
    "riskAssessment": "Two HIGH priority issues open 14 days — potential release blockers.",
    "recommendedActions": "Prioritize DB connection pool fix before next release."
  }
}
```

## Author

**Sai Kumar Moguluri**
LinkedIn: [linkedin.com/in/sai-1899k](https://linkedin.com/in/sai-1899k)
GitHub: [github.com/sai-k21](https://github.com/sai-k21)
