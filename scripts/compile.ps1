$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$jdk = Join-Path $projectRoot "tools\jdk21\jdk-21.0.11+10"
$target = Join-Path $projectRoot "target\classes"
$sourcesFile = Join-Path $projectRoot "target\sources.txt"

if (Test-Path $jdk) {
    $env:JAVA_HOME = $jdk
    $env:Path = "$jdk\bin;$env:Path"
}

New-Item -ItemType Directory -Force -Path $target | Out-Null
Get-ChildItem -Path (Join-Path $projectRoot "src\main\java") -Recurse -Filter *.java |
    ForEach-Object { $_.FullName } |
    Set-Content -Path $sourcesFile -Encoding ASCII

javac --release 17 -encoding UTF-8 -cp "$projectRoot\lib\*" -d $target "@$sourcesFile"
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}
