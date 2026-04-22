param(
    [switch]$SkipFrontend,
    [switch]$SkipSmoke,
    [string]$GatewayUrl = "http://localhost:8090"
)

$ErrorActionPreference = "Stop"

function Step([string]$Title, [scriptblock]$Action) {
    Write-Host ""
    Write-Host "==> $Title" -ForegroundColor Cyan
    & $Action
}

Step "Backend compile (all modules)" {
    .\gradlew.bat compileJava | Out-Host
}

Step "Backend tests" {
    .\gradlew.bat test | Out-Host
}

if (-not $SkipFrontend) {
    Step "Frontend install" {
        Push-Location car-service-frontend
        try {
            npm ci | Out-Host
        } finally {
            Pop-Location
        }
    }

    Step "Frontend build" {
        Push-Location car-service-frontend
        try {
            npm run build | Out-Host
        } finally {
            Pop-Location
        }
    }
}

Step "Service readiness check" {
    powershell -ExecutionPolicy Bypass -File .\scripts\check-services.ps1 | Out-Host
}

if (-not $SkipSmoke) {
    Step "Smoke e2e notifications chain" {
        powershell -ExecutionPolicy Bypass -File .\scripts\smoke-e2e-notifications.ps1 -BaseUrl $GatewayUrl | Out-Host
    }
}

Write-Host ""
Write-Host "QA checks finished successfully." -ForegroundColor Green
