# SkaETL

SkaLogs ETL is a unique real time ETL designed for and dedicated to Logs and Events.
 
[![Build Status](https://travis-ci.com/skalogs/SkaETL.svg?branch=master)](https://travis-ci.com/skalogs/SkaETL)

Core features :

 * Centralized Logstash Configuration
 * Log Parsing Simulations based on extensive list of common pre-set patterns
 * Consumer Processes: Ingestion Pipeline handling through guided workflow
   * Ingestion, Parsing, Transformation, Metrics, Notifications, Parsing Simulations
   * Parsing: ability to handle multiple input formats: CEF (HP Arcsight/MicroFocus), Nitro (MacAfee), GROK, CSV, json as string
 * Build data referential on the fly based on events processed by SkaETL
 * Build metrics on the fly (standard statistical & count functions): before storing in ES (avoids computations in ES, reduces ressources dedicated to ES cluster)
   * Create new mathematical functions to extend standard statistical metrics
 * Create threshold and notifications
 * Preview live data (before storing and indexing in ES)
   * At ingestion in Kafka
   * After Parsing
   * After Transforming
 * Output: ES, Kafka
 * Notifications: email, Slack
 
 SkaETL parses and enhances data from Kafka topics to any output :
 
 * Kafka (enhanced topics)
 * Elasticsearch
 * Notfications : email, Slack
 * more to come...

Detailed features :

 * Real Time: real-time streaming, transformation, analysis, standardization, calculations and visualization of all your        ingested and processed data
 * Guided Workflows:
   * "consumer processes" (data ingestion pipelines) to guide you through transformation, normalization, analysis - avoiding      the tedious task of transforming different types of Logs via Logstash
   * Optional metrics computations via simple functions or complex customized functions via SkaLogs Language
   * Optional alerts and notifications
   * Referentials creation for further reuse
 * Logstash Configuration Generator:  on the fly Logstash configuration generator
 * Parsing: grok, nitro, cef, with simulation tool
 * Error Retry Mechanism: automated mechanism for re-processing data ingestion errors
 * Referentials: create referentials for further reuse
 * CMDB: create IT inventory referential
 * Computations (Metrics): precompute all your metrics before storing your results in ES (reduces the use of ES resources and    the # ES licenses),
 * SkaLogs Language: Complex queries, event correlations (SIEM) and calculations, with an easy-to-use SQL-like language
 * Monitoring - Alerts: Real-time monitoring, alerts and notifications based on events and thresholds
 * Visualization: dashboard to monitor in real-time all your ingestion processes, metrics, referentials, kafka live stream
 * Output: Kafka, ES, email, Slack, more to come...

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
