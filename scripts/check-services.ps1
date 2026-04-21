param(
    [string]$HostName = "localhost"
)

$ErrorActionPreference = "Stop"

$targets = @(
    @{ Name = "gateway"; Port = 8090; Health = "http://$HostName:8090/actuator/health" },
    @{ Name = "claim"; Port = 8081; Health = "http://$HostName:8081/actuator/health" },
    @{ Name = "client"; Port = 8082; Health = "http://$HostName:8082/actuator/health" },
    @{ Name = "master"; Port = 8083; Health = "http://$HostName:8083/actuator/health" },
    @{ Name = "warehouse"; Port = 8084; Health = "http://$HostName:8084/actuator/health" },
    @{ Name = "notification"; Port = 8085; Health = "http://$HostName:8085/actuator/health" },
    @{ Name = "nsi"; Port = 8086; Health = "http://$HostName:8086/actuator/health" }
)

$failed = @()

foreach ($t in $targets) {
    $portOk = $false
    try {
        $conn = Test-NetConnection -ComputerName $HostName -Port $t.Port -WarningAction SilentlyContinue
        $portOk = [bool]$conn.TcpTestSucceeded
    } catch {
        $portOk = $false
    }

    if (-not $portOk) {
        Write-Host ("[FAIL] {0}: port {1} is closed" -f $t.Name, $t.Port) -ForegroundColor Red
        $failed += $t.Name
        continue
    }

    try {
        $res = Invoke-WebRequest -Uri $t.Health -Method GET -TimeoutSec 5 -UseBasicParsing
        if ($res.StatusCode -ge 200 -and $res.StatusCode -lt 300) {
            Write-Host ("[OK]   {0}: port {1}, health {2}" -f $t.Name, $t.Port, $res.StatusCode) -ForegroundColor Green
        } else {
            Write-Host ("[WARN] {0}: port {1} open, health {2}" -f $t.Name, $t.Port, $res.StatusCode) -ForegroundColor Yellow
        }
    } catch {
        Write-Host ("[WARN] {0}: port {1} open, health endpoint unavailable" -f $t.Name, $t.Port) -ForegroundColor Yellow
    }
}

if ($failed.Count -gt 0) {
    Write-Host ""
    Write-Host ("Not ready: " + ($failed -join ", ")) -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "All required service ports are reachable." -ForegroundColor Green
exit 0
