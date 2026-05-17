#!/usr/bin/env bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
TARGET_DIR="$PROJECT_ROOT/target/classes"
SOURCES_FILE="$PROJECT_ROOT/target/sources.txt"

mkdir -p "$TARGET_DIR"
find "$PROJECT_ROOT/src/main/java" -name "*.java" > "$SOURCES_FILE"

javac --release 17 -encoding UTF-8 -d "$TARGET_DIR" @"$SOURCES_FILE"
