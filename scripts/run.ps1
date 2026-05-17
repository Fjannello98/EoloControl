$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$jdk = Join-Path $projectRoot "tools\jdk21\jdk-21.0.11+10"

if (Test-Path $jdk) {
    $env:JAVA_HOME = $jdk
    $env:Path = "$jdk\bin;$env:Path"
}

& (Join-Path $PSScriptRoot "compile.ps1")
java -cp "$projectRoot\target\classes;$projectRoot\lib\*" eolocontrol.SwingApp
