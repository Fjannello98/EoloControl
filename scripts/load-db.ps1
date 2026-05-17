$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$mysqlClient = Join-Path $projectRoot "tools\mysql\mysql-8.4.9-winx64\bin\mysql.exe"

if (-not (Test-Path $mysqlClient)) {
    $mysqlClient = "mysql"
}

Get-Content -Raw (Join-Path $projectRoot "sql\01_schema.sql") | & $mysqlClient -u root --protocol=TCP
Get-Content -Raw (Join-Path $projectRoot "sql\02_seed.sql") | & $mysqlClient -u root --protocol=TCP
Get-Content -Raw (Join-Path $projectRoot "sql\03_queries.sql") | & $mysqlClient -u root --protocol=TCP
