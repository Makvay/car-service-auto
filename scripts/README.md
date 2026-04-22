# Smoke/Readiness scripts

## 1) Check service readiness

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\check-services.ps1
```

Checks TCP ports for gateway + all backend services and probes `/actuator/health`.

## 2) Notification e2e smoke chain

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\smoke-e2e-notifications.ps1
```

Runs:

1. `POST /api/claims`
2. `PUT /api/claims/{id}/master`
3. `PUT /api/claims/{id}/status` -> `IN_PROGRESS`
4. `PUT /api/claims/{id}/status` -> `COMPLETED`

Then verify mail delivery in mailbox and notification-service logs.

## 3) Full QA gate (one command)

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\qa.ps1
```

Runs:

1. Backend compile (`gradlew compileJava`)
2. Backend tests (`gradlew test`)
3. Frontend install + production build
4. Service readiness check
5. E2E smoke chain for claims/notifications

Optional flags:

```powershell
# Skip frontend build
powershell -ExecutionPolicy Bypass -File .\scripts\qa.ps1 -SkipFrontend

# Skip smoke scenario
powershell -ExecutionPolicy Bypass -File .\scripts\qa.ps1 -SkipSmoke
```
