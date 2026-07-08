#!/bin/bash

# This script builds the runtime images from the various JDKs, which must first be
# downloaded from https://jdk.java.net and expanded into ./jdks.

modpath=target/weavedreamer-0.2.10.02-PRERELEASE.jar
modpath=${modpath}:$HOME/.m2/repository/org/apache/commons/commons-configuration2/2.13.0/commons-configuration2-2.13.0.jar
modpath=${modpath}:$HOME/.m2/repository/org/apache/commons/commons-lang3/3.20.0/commons-lang3-3.20.0.jar
modpath=${modpath}:$HOME/.m2/repository/commons-logging/commons-logging/1.3.5/commons-logging-1.3.5.jar
modpath=${modpath}:$HOME/.m2/repository/org/apache/commons/commons-text/1.14.0/commons-text-1.14.0.jar


for arch in linux_arm linux_x64 mac_x64 windows mac_arm; do
  # jmods sits at jdk-*/jmods on linux/windows/mac_x64 but under the
  # Contents/Home bundle on the mac_arm .jdk, so locate it per-arch.
  jmods=$(find "jdks/${arch}" -type d -name jmods | head -n1)
  if [ -z "$jmods" ]; then
    echo "ERROR: no jmods found for $arch under jdks/${arch}" >&2
    exit 1
  fi
  rm -rf runtime/$arch
  jlink  --module-path "${jmods}:$modpath" \
         --output runtime/$arch \
         --add-modules com.jenkins.weavedreamer \
         --launcher weavedreamer=com.jenkins.weavedreamer/com.jenkins.weavedreamer.WeaveDreamerApp \
         --compress zip-6 --no-header-files --no-man-pages --strip-debug
done

