#!/usr/bin/env bash
set -euo pipefail

project_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
if [[ -f "$project_root/.env" ]]; then
  set -a
  source "$project_root/.env"
  set +a
fi

if [[ $# -eq 0 ]]; then
  set -- spring-boot:run
fi
exec "$project_root/backend/mvnw" -f "$project_root/backend/pom.xml" "$@"
