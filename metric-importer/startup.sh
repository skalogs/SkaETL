#!/bin/bash
set -e

JAVA_OPTS=${JAVA_OPTS:="-Xmx256m"}

/usr/bin/java -jar ${JAVA_OPTS} /usr/share/skalogs/metric-importer.jar
