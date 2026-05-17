#!/usr/bin/env bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
MYSQL_USER="${MYSQL_USER:-root}"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-}"

if [[ -n "$MYSQL_PASSWORD" ]]; then
  MYSQL_CMD=(mysql -u "$MYSQL_USER" "-p$MYSQL_PASSWORD")
else
  MYSQL_CMD=(mysql -u "$MYSQL_USER")
fi

"${MYSQL_CMD[@]}" < "$PROJECT_ROOT/sql/01_schema.sql"
"${MYSQL_CMD[@]}" < "$PROJECT_ROOT/sql/02_seed.sql"
"${MYSQL_CMD[@]}" < "$PROJECT_ROOT/sql/03_queries.sql"
