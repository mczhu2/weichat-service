param(
    [string]$Mode = "voice_base64",
    [string]$Base64File = ""
)

$ErrorActionPreference = "Stop"

$apiDir = $PSScriptRoot
1..7 | ForEach-Object {
    $apiDir = Split-Path -Parent $apiDir
}
$runtimeClasspathFile = Join-Path $apiDir "target\runtime-classpath.txt"
$mainClasses = Join-Path $apiDir "target\classes"
$testClasses = Join-Path $apiDir "target\test-classes"
$sourceFile = Join-Path $PSScriptRoot "CustomerReplyServiceManualRunner.java"

if (-not (Test-Path $runtimeClasspathFile)) {
    throw "Missing runtime classpath file: $runtimeClasspathFile"
}

if (-not (Test-Path $mainClasses)) {
    throw "Missing main classes directory: $mainClasses"
}

New-Item -ItemType Directory -Force -Path $testClasses | Out-Null

$runtimeClasspath = (Get-Content $runtimeClasspathFile -Raw).Trim()
$compileClasspath = "$runtimeClasspath;$mainClasses"
$runClasspath = "$runtimeClasspath;$testClasses;$mainClasses"

& javac --release 8 -encoding UTF-8 -cp $compileClasspath -d $testClasses $sourceFile
if ($LASTEXITCODE -ne 0) {
    throw "javac failed"
}

$javaArgs = @("-cp", $runClasspath, "com.weichat.api.testsupport.CustomerReplyServiceManualRunner", $Mode)
if ($Base64File) {
    $javaArgs += $Base64File
}

& java @javaArgs
if ($LASTEXITCODE -ne 0) {
    throw "runner failed"
}
