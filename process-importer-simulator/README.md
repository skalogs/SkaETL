# Simulate Importer

This service read into topic process from kafka and send data to ES in index. The index are calculated on the attribute "type" from the message. 

## Install & Run
```
mvn clean package
java -jar target/process-importer*
```

## Launch process retry

## Config
```
src/main/ressources/application
```

You can override all variable via ENV SYSTEM (doc https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)

### Config Validator

The message send into Kafka MUST BE a JSON.

* validator.maxFields: max field allow
* validator.maxSize: max size message
* validator.maximumInThePastAllowed: number of days
* validator.maximumInTheFutureAllowed: number of days
* validator.mandatoryFields: list of mandatory field into message
```
example
- "@timestamp"
- "type"
```

### Config elasticsearch

* elasticsearch.host: localhost
* elasticsearch.port: 9300
* elasticsearch.clusterName: docker-cluster
* elasticsearch.serviceElasticsearchUsername: elastic
* elasticsearch.serviceElasticsearchPassword: changeme
* elasticsearch.clientTransportPingTimeout: 10
* elasticsearch.clientNodesSamplerInterval: 10
* elasticsearch.customIndexPrefix: All index create by Importer are prefixed via this parameter

### Config kafka

* kafka.retryTopic: retrytopic
* kafka.errorTopic: errorTopic
* kafka.bootstrapServers: localhost:9092
* kafka.pollingTime: 1000
* kafka.pollRecord: 50
* kafka.groupIdRetry: retryImporter

## Generate Fake data for test
```
http://localhost:808080/apigenerator/inputTopic
```