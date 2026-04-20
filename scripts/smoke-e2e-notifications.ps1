param(
    [string]$BaseUrl = "http://localhost:8081",
    [int]$ClientId = 1,
    [int]$MasterId = 1,
    [int]$VehicleId = 1
)

$ErrorActionPreference = "Stop"

function Invoke-JsonRequest {
    param(
        [string]$Method,
        [string]$Url,
        [object]$Body = $null
    )

    if ($null -ne $Body) {
        $json = $Body | ConvertTo-Json -Depth 10
        return Invoke-RestMethod -Method $Method -Uri $Url -ContentType "application/json" -Body $json
    }

    return Invoke-RestMethod -Method $Method -Uri $Url
}

Write-Host "Running claim->status smoke chain against $BaseUrl ..." -ForegroundColor Cyan

$createPayload = @{
    clientId = $ClientId
    vehicleId = $VehicleId
    mileageAtEntry = 1000
    problemDescription = "Smoke test notification flow"
    priority = "NORMAL"
}

$created = Invoke-JsonRequest -Method "POST" -Url "$BaseUrl/api/claims" -Body $createPayload
if (-not $created.id) {
    throw "Claim was not created."
}

$claimId = [int]$created.id
Write-Host ("Created claim id={0}, number={1}" -f $claimId, $created.claimNumber) -ForegroundColor Green

$null = Invoke-JsonRequest -Method "PUT" -Url "$BaseUrl/api/claims/$claimId/master?masterId=$MasterId"
Write-Host ("Assigned master id={0}" -f $MasterId) -ForegroundColor Green

$null = Invoke-JsonRequest -Method "PUT" -Url "$BaseUrl/api/claims/$claimId/status" -Body @{ status = "IN_PROGRESS" }
Write-Host "Status -> IN_PROGRESS" -ForegroundColor Green

$null = Invoke-JsonRequest -Method "PUT" -Url "$BaseUrl/api/claims/$claimId/status" -Body @{ status = "COMPLETED" }
Write-Host "Status -> COMPLETED" -ForegroundColor Green

Write-Host ""
Write-Host "Smoke chain finished successfully." -ForegroundColor Green
Write-Host "Check notification-service logs/email inbox for 3 notifications: CREATED, IN_PROGRESS, COMPLETED."
