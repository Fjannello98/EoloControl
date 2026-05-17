$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$mysqlHome = Join-Path $projectRoot "tools\mysql\mysql-8.4.9-winx64"
$dataDir = Join-Path $projectRoot "mysql-data"
$stdout = Join-Path $dataDir "mysqld-runtime.out.log"
$stderr = Join-Path $dataDir "mysqld-runtime.err.log"

if (-not (Test-Path $mysqlHome)) {
    throw "No se encontro MySQL portable en tools\mysql."
}

if (-not (Test-Path $dataDir)) {
    New-Item -ItemType Directory -Force -Path $dataDir | Out-Null
    & "$mysqlHome\bin\mysqld.exe" --initialize-insecure --basedir="$mysqlHome" --datadir="$dataDir"
}

$existing = Get-Process mysqld -ErrorAction SilentlyContinue | Where-Object { $_.Path -like "$mysqlHome*" }
if (-not $existing) {
    Start-Process `
        -FilePath "$mysqlHome\bin\mysqld.exe" `
        -ArgumentList @("--basedir=$mysqlHome", "--datadir=$dataDir", "--port=3306", "--console") `
        -RedirectStandardOutput $stdout `
        -RedirectStandardError $stderr `
        -WindowStyle Hidden
    Start-Sleep -Seconds 5
}

Get-Process mysqld -ErrorAction SilentlyContinue | Select-Object Id, Path
