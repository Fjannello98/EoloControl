$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$jdk = Join-Path $projectRoot "tools\jdk21\jdk-21.0.11+10"
$maven = Join-Path $projectRoot "tools\maven\apache-maven-3.9.15\bin"

if (Test-Path $jdk) {
    $env:JAVA_HOME = $jdk
    $env:Path = "$jdk\bin;$env:Path"
}

if (Test-Path $maven) {
    $env:Path = "$maven;$env:Path"
}

mvn exec:java
