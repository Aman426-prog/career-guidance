# Career Guidance

A career readiness tracker: a Spring Boot backend plus a single-page frontend, served together. Inspired by the open-source CareerCompass project (https://github.com/codemasteryash/CareerCompass).

Users register, pick a target job role, add skills (by hand or by scanning a PDF resume), and get a readiness score, a skill-gap list, a learning roadmap, certification suggestions and portfolio project ideas.

## What is kept and what is simplified

| Area | Full-size version | Career Guidance |
| --- | --- | --- |
| Auth | Spring Security + JWT | Same (JJWT 0.12.6, BCrypt) |
| Skills / roles | Catalogue + user skills | Same, seeded on first start |
| Resume upload | PDF text, AI skill extraction, alias matching | PDFBox text + whole-word alias matching (no AI) |
| Analysis | Live skill gap + readiness score | Same, weighted High 3 / Medium 2 / Bonus 1 |
| Roadmap | AI-generated, cached per role | Missing skills ordered by priority |
| Certifications / projects | AI-recommended | Fixed catalogue in the seeder |
| Mentor chat, email, dashboard | Yes | Removed |
| Database | PostgreSQL (Supabase) | File-based H2 (PostgreSQL optional) |
| Frontend | Separate React app | One `index.html`, served by Spring Boot |

## Run it

Requirements: Java 21 and Maven 3.9+.

```
mvn spring-boot:run
```

Open http://localhost:8080, create an account and start. The H2 database is stored in `./data`, and the catalogue is seeded automatically on first start.

Optional environment variables: `JWT_SECRET` (at least 32 characters) and `JWT_EXPIRATION` (milliseconds).

## API

All endpoints except `/api/auth/*` need `Authorization: Bearer <token>`. Errors are returned as `{"error": "message"}`.

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/auth/register` | Create account, returns token |
| POST | `/api/auth/login` | Log in, returns token |
| GET | `/api/roles` | Job roles |
| GET | `/api/users/me` | Current profile |
| PUT | `/api/users/me/role` | Set target role `{roleId}` |
| GET | `/api/skills` | Skill catalogue |
| GET / POST | `/api/skills/me` | My skills / add one `{skillId}` |
| DELETE | `/api/skills/me/{skillId}` | Remove a skill |
| POST | `/api/resume` | Upload PDF (`file`), adds skills found |
| GET | `/api/analysis` | Score, status, covered and missing skills |
| GET | `/api/roadmap` | Missing skills in learning order |
| GET | `/api/certifications/recommendations` | Certifications for your gaps |
| GET | `/api/projects/recommendations` | Project ideas for your role |

## Structure

```
src/main/java/com/careerguidance/app/
  domain/    entities (AppUser, Skill, JobRole, RoleSkill, UserSkill, ProjectIdea)
  repo/      Spring Data repositories
  security/  JwtService, JwtFilter, SecurityConfig
  service/   CareerService (score, gaps), SkillExtractor, DataSeeder
  web/       controllers, DTO records, error handler
src/main/resources/
  application.properties
  static/index.html   the frontend
```
