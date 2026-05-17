#!/usr/bin/env bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

"$PROJECT_ROOT/scripts/compile.sh"
java -cp "$PROJECT_ROOT/target/classes:$PROJECT_ROOT/lib/*" eolocontrol.SwingApp
