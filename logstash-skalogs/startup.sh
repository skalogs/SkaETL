#!/bin/bash
# You must define the LOGSTASH_HOME : path to install logstash (example /usr/share/logstash)
# You must define the LOGSTASH_SKALOGS_URL with the administration console

set -e

if [ -z ${LOGSTASH_SKALOGS_URL} ]; then
    echo 'LOGSTASH_SKALOGS_URL is not defined !'
else
    if [ -z ${LOGSTASH_HOME} ]; then
        echo 'LOGSTASH_HOME is not defined !'
    else
        mkdir -p ${LOGSTASH_HOME}/skalogs
        chmod 755 ${LOGSTASH_HOME}/skalogs
        curl -o ${LOGSTASH_HOME}/skalogs/logstash_skalogs.yml ${LOGSTASH_SKALOGS_URL}
        ${LOGSTASH_HOME}/bin/logstash -f ${LOGSTASH_HOME}/skalogs/logstash_skalogs.yml
    fi
fi
