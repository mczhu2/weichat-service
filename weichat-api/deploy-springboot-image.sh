#!/usr/bin/env bash
set -euo pipefail

IMAGE_META="${IMAGE_META:-/home/admin/app/package.env}"
CONTAINER="${CONTAINER:-weichat-api}"
ENV_FILE="${ENV_FILE:-/home/admin/app/weichat-api.env}"
NETWORK="${NETWORK:-weichat-api-net}"
HOST_PORT="${HOST_PORT:-8066}"
CONTAINER_PORT="${CONTAINER_PORT:-8066}"
SPRING_PROFILE="${SPRING_PROFILE:-prod}"
TZ_VALUE="${TZ_VALUE:-Asia/Shanghai}"
HEALTH_PATH="${HEALTH_PATH:-}"
STARTUP_TIMEOUT="${STARTUP_TIMEOUT:-60}"

echo "[1/5] check files"
test -f "$IMAGE_META"
test -f "$ENV_FILE"

echo "[2/5] resolve image"
IMAGE="$(grep '^docker_url=' "$IMAGE_META" | head -n1 | cut -d= -f2-)"
test -n "$IMAGE"
echo "image=$IMAGE"

echo "[3/5] ensure network + pull image"
docker network inspect "$NETWORK" >/dev/null 2>&1 || docker network create "$NETWORK"
docker pull "$IMAGE"

echo "[4/5] replace container"
docker rm -f "$CONTAINER" >/dev/null 2>&1 || true
docker run -d \
  --name "$CONTAINER" \
  --restart unless-stopped \
  --network "$NETWORK" \
  --env-file "$ENV_FILE" \
  -e SPRING_PROFILES_ACTIVE="$SPRING_PROFILE" \
  -e TZ="$TZ_VALUE" \
  -p "${HOST_PORT}:${CONTAINER_PORT}" \
  "$IMAGE" >/dev/null

echo "[5/5] verify"
for ((i = 1; i <= STARTUP_TIMEOUT; i++)); do
  if [[ "$(docker inspect -f '{{.State.Running}}' "$CONTAINER" 2>/dev/null || true)" != "true" ]]; then
    echo "container exited unexpectedly"
    docker logs --tail 200 "$CONTAINER" || true
    exit 1
  fi

  if [[ -n "$HEALTH_PATH" ]] && command -v curl >/dev/null 2>&1; then
    if curl -fsS "http://127.0.0.1:${HOST_PORT}${HEALTH_PATH}" >/dev/null; then
      echo "deploy ok"
      exit 0
    fi
  else
    if docker ps --filter "name=^/${CONTAINER}$" --filter "status=running" --format '{{.Names}}' | grep -qx "$CONTAINER"; then
      echo "deploy ok"
      exit 0
    fi
  fi

  sleep 2
done

echo "deploy verify timeout"
docker logs --tail 200 "$CONTAINER" || true
exit 1
