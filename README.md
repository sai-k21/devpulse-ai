# DevPulse AI

AI-powered developer productivity API that analyzes GitHub repositories using Google Gemini to generate standup summaries, code health scores, risk assessments, and recommended actions.

## Live Demo
API is deployed and running. Swagger UI available at the deployed URL.

## Features
- AI-generated daily standup summaries from commit history
- Code health score with explanation
- Risk assessment from open issues
- Recommended actions for the engineering team
- Real-time metrics dashboard endpoint
- Spring Boot Actuator health and metrics endpoints
- Swagger/OpenAPI 3 documentation
- Dockerized with Docker Compose
- GitHub Actions CI/CD pipeline

## Tech Stack
| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5 |
| AI | Google Gemini API (gemini-2.0-flash-lite) |
| Containerization | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| API Docs | Swagger UI / OpenAPI 3 |

## API Endpoints
| Method | Endpoint | Description |
|---|---|---|
| GET | /api/v1/health | Health check |
| POST | /api/v1/analyze | Analyze a GitHub repository with AI |
| GET | /api/v1/analyze/demo | Demo analysis with sample data |

## Running Locally

### Prerequisites
- Java 17+
- Maven 3.9+
- Gemini API key from aistudio.google.com

### Run
```bash
$env:GEMINI_API_KEY="your_key_here"; mvn spring-boot:run
```

Visit: http://localhost:8080/swagger-ui.html

## Architecture