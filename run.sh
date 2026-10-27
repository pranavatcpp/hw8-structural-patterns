#!/bin/sh
set -eu
cd "$(dirname "$0")"

mkdir -p build
javac -Xlint:all -d build src/bridge/*.java src/decorator/*.java

printf '=== Bridge payment notifications ===\n'
java -cp build bridge.BridgeDriver
printf '\n=== Decorator restaurant orders ===\n'
java -cp build decorator.DecoratorDriver
