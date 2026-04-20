# GitHub Publish Checklist

## 1. Verify no local secrets are committed

- Mail credentials must come from env vars only.
- `.env` must stay local and ignored.
- Keep `.env.example` with placeholders.

Quick check:

```powershell
git grep -n -E "MAIL_PASSWORD|MAIL_USERNAME|token|secret|password"
```

## 2. Verify repository is clean from local/IDE artifacts

Ignored by `.gitignore`:

- `.idea/`
- `*.log`, `*.err.log`, `*.out.log`
- `temp_*.json`, `temp_*.sql`
- `.env`, `.env.local`

## 3. Build sanity before push

```powershell
.\gradlew :common-service-parent:client-microservice:compileJava :common-service-parent:claim-microservice:compileJava
```

Optional smoke:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\check-services.ps1
powershell -ExecutionPolicy Bypass -File .\scripts\smoke-e2e-notifications.ps1
```

## 4. Commit and push

```powershell
git add .
git commit -m "Prepare project for GitHub publication"
git push origin main
```
