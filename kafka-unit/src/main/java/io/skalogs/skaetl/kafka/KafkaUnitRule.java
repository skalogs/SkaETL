package io.skalogs.skaetl.kafka;

import org.junit.rules.ExternalResource;

import java.io.IOException;

public class KafkaUnitRule extends ExternalResource {

    private final KafkaUnit kafkaUnit;

    public KafkaUnitRule() {
        try {
            this.kafkaUnit = new KafkaUnit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public KafkaUnitRule(int zkPort, int kafkaPort) {
        this.kafkaUnit = new KafkaUnit(zkPort, kafkaPort);
    }

    public KafkaUnitRule(String zkConnectionString, String kafkaConnectionString) {
        this.kafkaUnit = new KafkaUnit(zkConnectionString, kafkaConnectionString);
    }

    public KafkaUnitRule(int zkPort, int kafkaPort, int zkMaxConnections) {
        this.kafkaUnit = new KafkaUnit(zkPort, kafkaPort, zkMaxConnections);
    }

    public KafkaUnitRule(String zkConnectionString, String kafkaConnectionString, int zkMaxConnections) {
        this.kafkaUnit = new KafkaUnit(zkConnectionString, kafkaConnectionString, zkMaxConnections);
    }

    @Override
    protected void before() throws Throwable {
        kafkaUnit.startup();
    }

    @Override
    protected void after() {
        kafkaUnit.shutdown();
    }

    public int getZkPort() {
        return kafkaUnit.getZkPort();
    }

    public int getKafkaPort() {
        return kafkaUnit.getBrokerPort();
    }

    public KafkaUnit getKafkaUnit() {
        return kafkaUnit;
    }
}