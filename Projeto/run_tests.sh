#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

JAVAC_CMD="javac"
JAVA_CMD="java"
FIND_CMD=(find src -name "*.java")

if [[ -n "${JAVAFX_HOME:-}" && -d "${JAVAFX_HOME}/lib" ]]; then
  JAVAC_CMD="javac --module-path ${JAVAFX_HOME}/lib --add-modules javafx.controls"
  JAVA_CMD="java --module-path ${JAVAFX_HOME}/lib --add-modules javafx.controls"
else
  FIND_CMD=(find src -name "*.java" ! -path "*/ui/fx/*")
fi

# compile main sources
${FIND_CMD[@]} -print0 | xargs -0 ${JAVAC_CMD} -encoding UTF-8 -d out
# compile tests
find src/test -name "*.java" -print0 | xargs -0 ${JAVAC_CMD} -encoding UTF-8 -d out -cp out
# run test
${JAVA_CMD} -cp out pt.escnaval.exercicios.manutencao.TestServicoManutencao
echo "Tests completed"
