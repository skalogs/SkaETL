# SkaETL

SkaLogs ETL is a unique real time ETL designed for and dedicated to Logs and Events.
 
[![Build Status](https://travis-ci.com/skalogs/SkaETL.svg?branch=master)](https://travis-ci.com/skalogs/SkaETL)

Core features :

 * Centralized Logstash Configuration
 * Ingestion Pipeline handling through guided workflow
 * Build data referential on the fly based on events processed by SkaETL
 * Build metrics on the fly

Detailed features :

 * REAL TIME: real-time streaming, transformation, analysis, standardization, calculations and visualization of all your        ingested and processed data
 * GUIDED WORKFLOWS:
   * "consumer processes" (data ingestion pipelines) to guide you through transformation, normalization, analysis - avoiding      the tedious task of transforming different types of Logs via Logstash
   * Optional metrics computations via simple functions or complex customized functions via SkaLogs Language
   * Optional alerts and notifications
   * Referentials creation for further reuse
 * LOGSTASH CONFIGURATION GENERATOR:  on the fly Logstash configuration generator
 * PARSING: grok, nitro, cef, with simulation tool
 * ERROR RETRY MECHANISM: automated mechanism for re-processing data ingestion errors
 * REFERENTIALS: create referentials for further reuse
 * CMDB: create IT inventory referential
 * COMPUTATIONS (METRICS): precompute all your metrics before storing your results in ES (reduces the use of ES resources and    the # ES licenses),
 * SKALOGS LANGUAGE: Complex queries, event correlations (SIEM) and calculations, with an easy-to-use SQL-like language
 * MONITORING - ALERTS: Real-time monitoring, alerts and notifications based on events and thresholds
 * VISUALIZATION: dashboard to monitor in real-time all your ingestion processes, metrics, referentials, kafka live stream
 * OUTPUT: Kafka, ES, email, Slack, more to come...
 
SkaETL parses and enhances data from Kafka topics to any output :
* Kafka (enhanced topics)
* Elasticsearch
* Email
* Slack
* more to come...

## Requirements

* Java >= 1.8 
* Kafka 1.0.0


## Building the Source

SkaETL is built using [Apache Maven](http://maven.apache.org/).

Build the full project and run tests: 
```sh
$ mvn clean install
```

Build without tests: 
```sh
$ mvn clean install -DskipTests
```

## License

SkaETL is released under Apache License 2.0.
