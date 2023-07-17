#!/usr/bin/env bash
# Go to current working directory
WORKING_DIR="$(pwd)"
cd "$WORKING_DIR"

# Build flutter web
cd ./app || exit
echo "Building flutter web..."
flutter build web || exit
cd "$WORKING_DIR" || exit

# Build plugin
cd ./plugin || exit
echo "Building plugin"
gradle clean buildRelease || exit
cd "$WORKING_DIR" || exit

# Build adapters
# list all directories in "./adapters"
for adapter in ./adapters/*; do
  # check if it's a directory
  if [ -d "$adapter" ]; then
    # go to adapter directory
    cd "$adapter" || exit
    echo "Building adapter: $adapter"
    # build adapter
    gradle clean buildRelease || exit
    # go back to working directory
    cd "$WORKING_DIR" || exit
  fi
done