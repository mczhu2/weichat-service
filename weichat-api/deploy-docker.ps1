param(
    [string]$ImageName = "weichat-api",
    [string]$ContainerName = "weichat-api",
    [int]$HostPort = 8066,
    [int]$ContainerPort = 8066
)

$ErrorActionPreference = 'Stop'

$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot

mvn -pl weichat-api -am -DskipTests package
if ($LASTEXITCODE -ne 0) { throw "mvn package failed with exit code $LASTEXITCODE" }

docker build -t $ImageName -f weichat-api\Dockerfile .
if ($LASTEXITCODE -ne 0) { throw "docker build failed with exit code $LASTEXITCODE" }

docker rm -f $ContainerName 2>$null | Out-Null

docker run -d `
  --name $ContainerName `
  --restart unless-stopped `
  -p "$HostPort`:$ContainerPort" `
  -e "SPRING_PROFILES_ACTIVE=prod" `
  -e "TZ=Asia/Shanghai" `
  $ImageName

if ($LASTEXITCODE -ne 0) { throw "docker run failed with exit code $LASTEXITCODE" }
