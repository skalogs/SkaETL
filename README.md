# SkaETL

SkaLogs ETL is a unique real time ETL designed for and dedicated to Logs and Events.
 
[![Build Status](https://travis-ci.com/skalogs/SkaETL.svg?branch=master)](https://travis-ci.com/skalogs/SkaETL)

Core features :

 * Centralized Logstash Configuration
 * Ingestion Pipeline handling through guided workflow
 * Build data referential on the fly based on events processed by SkaETL
 * Build metrics on the fly  
 
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
