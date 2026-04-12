param(
    [string[]]$MessageIds = @("510", "512")
)

$ErrorActionPreference = "Stop"

$apiDir = $PSScriptRoot
1..7 | ForEach-Object {
    $apiDir = Split-Path -Parent $apiDir
}

$runtimeClasspathFile = Join-Path $apiDir "target\runtime-classpath.txt"
$mainClasses = Join-Path $apiDir "target\classes"
$testClasses = Join-Path $apiDir "target\test-classes"
$sourceFile = Join-Path $PSScriptRoot "DownstreamMessageContentManualRunner.java"

if (-not (Test-Path $runtimeClasspathFile)) {
    throw "Missing runtime classpath file: $runtimeClasspathFile"
}

New-Item -ItemType Directory -Force -Path $testClasses | Out-Null

$runtimeClasspath = (Get-Content $runtimeClasspathFile -Raw).Trim()
$compileClasspath = "$runtimeClasspath;$mainClasses"
$runClasspath = "$runtimeClasspath;$testClasses;$mainClasses"

& javac --release 8 -encoding UTF-8 -cp $compileClasspath -d $testClasses $sourceFile
if ($LASTEXITCODE -ne 0) {
    throw "javac failed"
}

$javaArgs = @("-cp", $runClasspath, "com.weichat.api.testsupport.DownstreamMessageContentManualRunner")
$javaArgs += $MessageIds

& java @javaArgs
if ($LASTEXITCODE -ne 0) {
    throw "runner failed"
}
