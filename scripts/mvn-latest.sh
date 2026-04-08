#!/usr/bin/env bash
set -euo pipefail

# Run Maven with the newest project-supported toolchain (Java 25 + local Maven 3.9.14).
export JAVA_HOME="/home/amier/.jdk/jdk-25"
export PATH="/home/amier/.maven/maven-3.9.14/bin:$JAVA_HOME/bin:$PATH"

exec mvn "$@"
