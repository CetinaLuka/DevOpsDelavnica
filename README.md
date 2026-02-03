# DevOps delavnica Demo

Content:
- Quarkus backend 
- React frontent
- CI/CD cevovod:
  - Build
  - Unit test automation
  - Quality Gate automation
  - Functional test automation
  - Containerized Deployment to a Cloud environment

## Scope (Backend + Frontend + CLI)
- App stores **Products** (id / name / min allowed temperature / max allowed termperature) in DB;
  **CRUD** operations are available on Products via REST endpoint,
- It enables sending **Measurements** (product / temperature / measurement type) via REST endpoint,
- When recorded, **measurements are marked** as OK (inside allowed temperature range) or NOT OK,
- **Measurements history** for last 10 days is available via REST endpoint.

## Running with Docker Compose (All Services)

To run the entire application stack (database, backend, and frontend) with Docker Compose:

1. Build the backend first (required for the Docker image):
```bash
cd backend
./mvnw package -DskipTests
cd ..
```

2. Build and run all services:
```bash
docker compose build
docker compose up
```

The services will be available at:
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8280/api/v1/swagger-ui/
- **Database**: localhost:5432

To stop all services:
```bash
docker compose down
```

## Running Backend
- build & run *or*
- docker-compose build & docker-compose up

Then check: http://127.0.0.1:8280/api/v1/swagger-ui/

For details check _backend_ folder.

## Running Frontend
- npm install & npm run *or*
- `docker run -d -p 3000:80 <image_name>`


For details check _frontend_ folder.
