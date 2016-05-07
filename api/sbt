#!/bin/sh

rm ./RUNNING_PID 2> /dev/null

java \
  -Xms512M \
  -Xmx896M \
  -XX:MaxMetaspaceSize=128M \
  -Dhttp.port=9000 \
  -Dfile.encoding=UTF-8 \
  $SBT_OPTS \
  -jar `dirname $0`/sbt-launch.jar \
  "$@"
