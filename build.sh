#!/bin/bash
# Borrowed from https://github.com/artem-zinnatullin/qualitymatters/blob/master/build.sh

set -xe

# You can run it from any directory.
PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# This will: compile the project, run lint, run tests under JVM, package apk, check the code quality and run tests on the device/emulator.
"$PROJECT_DIR"/gradlew --no-daemon --info --stacktrace clean
"$PROJECT_DIR"/gradlew --no-daemon --info --stacktrace build -PdisablePreDex -PwithDexcount -Dscan
"$PROJECT_DIR"/gradlew --no-daemon --info --stacktrace connectedAndroidTest -PdisablePreDex -PwithDexcount