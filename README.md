# SkaETL

SkaLogs ETL is a unique real time ETL designed for and dedicated to Logs and Events.
 
[![Build Status](https://travis-ci.com/skalogs/SkaETL.svg?branch=master)](https://travis-ci.com/skalogs/SkaETL)

Core features :

 * Centralized Logstash Configuration
 * Log Parsing Simulations based on extensive list of common pre-set patterns
 * Consumer Processes: Ingestion Pipeline handling through guided workflow
   * Ingestion (from specific Kafka topic)
   * Parsing: ability to handle multiple input formats:
     * CEF (HP Arcsight/MicroFocus),
     * Nitro (MacAfee),
     * GROK,
     * CSV,
     * [json as string](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/JSON/stringify)
   * Parsing Simulations (ability to simulate multiple preset grok patterns on a json log)
   * Transformation: add csv lookup, add field, add geolocalization, capitalize, delete field, format boolean, format date, format double, format email, format geopoint, format ip, format long, hash, lookup external, lookup list, lower case, rename field, swap case, trim, uncapitalize, upper case.
   * Metrics
     * functions: count, count-distinct, sum, avg, min, max, stddev, mean,
     * window types: tumbling, hopping, session,
     * time units: seconds, minutes, hours, days,
     * join types: none, inner, outer, left.
   * Notifications
     * email
     * Slack
 * Build data referential on the fly based on events processed by SkaETL
 * Build metrics on the fly (standard statistical & count functions): before storing in ES (avoids computations in ES, reduces ressources dedicated to ES cluster)
   * Create new mathematical functions to extend standard statistical metrics
 * Create threshold and notifications
 * Preview live data (before storing and indexing in ES)
   * At ingestion in [Kafka](https://kafka.apache.org)
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

 * Real Time: real-time streaming (Kafka, transformation, analysis, standardization, calculations and visualization of all ingested and processed data
 * Guided Workflows:
   * "consumer processes" (data ingestion pipelines) to guide you through transformation, normalization, analysis - avoiding      the tedious task of transforming different types of Logs via Logstash
   * Optional metrics computations via simple functions or complex customized functions via SkaLogs Language
   * Optional alerts and notifications
   * Referentials creation for further reuse
 * Logstash Configuration Generator:  on the fly Logstash configuration generator
 * Parsing: grok, nitro, cef, with simulation tool
 * Error Retry Mechanism: automated mechanism for re-processing data ingestion errors
 * Referentials: create referentials for further reuse
 * [CMDB](https://en.wikipedia.org/wiki/Configuration_management_database): create IT inventory referential
 * Computations (Metrics): precompute all your metrics before storing your results in ES (reduces the use of ES resources and    the # ES licenses),
 * SkaLogs Language: Complex queries, complex computations, event correlations ([SIEM](https://en.wikipedia.org/wiki/Security_information_and_event_management)) and calculations, with an easy-to-use SQL-like language
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
