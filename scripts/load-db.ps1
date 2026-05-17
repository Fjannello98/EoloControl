$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$mysqlClient = Join-Path $projectRoot "tools\mysql\mysql-8.4.9-winx64\bin\mysql.exe"
$mysqlUser = if ($env:MYSQL_USER) { $env:MYSQL_USER } else { "root" }
$mysqlPassword = if ($env:MYSQL_PASSWORD) { $env:MYSQL_PASSWORD } else { "" }

if (-not (Test-Path $mysqlClient)) {
    $mysqlClient = "mysql"
}

$args = @("-u", $mysqlUser, "--protocol=TCP")
if ($mysqlPassword) {
    $args += "-p$mysqlPassword"
}

Get-Content -Raw (Join-Path $projectRoot "sql\01_schema.sql") | & $mysqlClient @args
Get-Content -Raw (Join-Path $projectRoot "sql\02_seed.sql") | & $mysqlClient @args
Get-Content -Raw (Join-Path $projectRoot "sql\03_queries.sql") | & $mysqlClient @args
