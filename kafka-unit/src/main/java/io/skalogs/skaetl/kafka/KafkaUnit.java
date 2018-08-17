package io.skalogs.skaetl.kafka;

import kafka.admin.RackAwareMode;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;
import kafka.zk.AdminZkClient;
import kafka.zk.KafkaZkClient;
import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.JavaConversions;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class KafkaUnit {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaUnit.class);

    private final String zookeeperString;
    private final String brokerString;
    private final int zkPort;
    private final int brokerPort;
    private final Properties kafkaBrokerConfig = new Properties();
    private final int zkMaxConnections;

    private KafkaServerStartable broker;
    private Zookeeper zookeeper;
    private File logDir;

    public KafkaUnit() throws IOException {
        this(getEphemeralPort(), getEphemeralPort());
    }

    public KafkaUnit(int zkPort, int brokerPort) {
        this(zkPort, brokerPort, 16);
    }

    public KafkaUnit(String zkConnectionString, String kafkaConnectionString) {
        this(parseConnectionString(zkConnectionString), parseConnectionString(kafkaConnectionString));
    }

    public KafkaUnit(String zkConnectionString, String kafkaConnectionString, int zkMaxConnections) {
        this(parseConnectionString(zkConnectionString), parseConnectionString(kafkaConnectionString), zkMaxConnections);
    }

    public KafkaUnit(int zkPort, int brokerPort, int zkMaxConnections) {
        this.zkPort = zkPort;
        this.brokerPort = brokerPort;
        this.zookeeperString = "localhost:" + zkPort;
        this.brokerString = "localhost:" + brokerPort;
        this.zkMaxConnections = zkMaxConnections;
    }

    private <K, V> KafkaProducer<K, V> createProducer(String keySerializerClass, String valueSerializer) {
        final Properties props = new Properties();
        props.put("bootstrap.servers", brokerString);
        props.put("key.serializer", keySerializerClass);
        props.put("value.serializer", valueSerializer);
        return new KafkaProducer<>(props);
    }

    private static int parseConnectionString(String connectionString) {
        try {
            String[] hostPorts = connectionString.split(",");

            if (hostPorts.length != 1) {
                throw new IllegalArgumentException("Only one 'host:port' pair is allowed in connection string");
            }

            String[] hostPort = hostPorts[0].split(":");

            if (hostPort.length != 2) {
                throw new IllegalArgumentException("Invalid format of a 'host:port' pair");
            }

            if (!"localhost".equals(hostPort[0])) {
                throw new IllegalArgumentException("Only localhost is allowed for KafkaUnit");
            }

            return Integer.parseInt(hostPort[1]);
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse connectionString " + connectionString, e);
        }
    }

    private static int getEphemeralPort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    public void startup() {
        zookeeper = new Zookeeper(zkPort, zkMaxConnections);
        zookeeper.startup();

        try {
            logDir = Files.createTempDirectory("kafka").toFile();
        } catch (IOException e) {
            throw new RuntimeException("Unable to start Kafka", e);
        }
        logDir.deleteOnExit();
        Runtime.getRuntime().addShutdownHook(new Thread(getDeleteLogDirectoryAction()));
        kafkaBrokerConfig.setProperty("zookeeper.connect", zookeeperString);
        kafkaBrokerConfig.setProperty("broker.id", "1");
        kafkaBrokerConfig.setProperty("host.name", "localhost");
        kafkaBrokerConfig.setProperty("port", Integer.toString(brokerPort));
        kafkaBrokerConfig.setProperty("log.dir", logDir.getAbsolutePath());
        kafkaBrokerConfig.setProperty("log.flush.interval.messages", String.valueOf(1));
        kafkaBrokerConfig.setProperty("delete.topic.enable", String.valueOf(true));
        kafkaBrokerConfig.setProperty("offsets.topic.replication.factor", String.valueOf(1));
        kafkaBrokerConfig.setProperty("auto.create.topics.enable", String.valueOf(false));

        broker = new KafkaServerStartable(new KafkaConfig(kafkaBrokerConfig));
        broker.startup();
    }

    private Runnable getDeleteLogDirectoryAction() {
        return new Runnable() {
            @Override
            public void run() {
                if (logDir != null) {
                    try {
                        FileUtils.deleteDirectory(logDir);
                    } catch (IOException e) {
                        LOGGER.warn("Problems deleting temporary directory " + logDir.getAbsolutePath(), e);
                    }
                }
            }
        };
    }

    public String getKafkaConnect() {
        return brokerString;
    }

    public int getZkPort() {
        return zkPort;
    }

    public int getBrokerPort() {
        return brokerPort;
    }

    public void createTopic(String topicName) {
        createTopic(topicName, 1);
    }

    public void createTopic(String topicName, int numPartitions) {
        // setup

        String zookeeperHost = zookeeperString;
        Boolean isSucre = false;
        int sessionTimeoutMs = 200000;
        int connectionTimeoutMs = 15000;
        int maxInFlightRequests = 10;
        Time time = Time.SYSTEM;
        String metricGroup = "myGroup";
        String metricType = "myType";
        KafkaZkClient zkClient = KafkaZkClient.apply(zookeeperHost,isSucre,sessionTimeoutMs,
                connectionTimeoutMs,maxInFlightRequests,time,metricGroup,metricType);
        AdminZkClient adminZkClient = new AdminZkClient(zkClient);
        try {
            // run
            LOGGER.info("Executing: CreateTopic " + topicName);
            adminZkClient.createTopic(topicName,numPartitions, 1,new Properties(), RackAwareMode.Disabled$.MODULE$);
        } finally {
            zkClient.close();
        }

    }

    /**
     * @return All topic names
     */
    public List<String> listTopics() {
        String zookeeperHost = zookeeperString;
        Boolean isSucre = false;
        int sessionTimeoutMs = 200000;
        int connectionTimeoutMs = 15000;
        int maxInFlightRequests = 10;
        Time time = Time.SYSTEM;
        String metricGroup = "myGroup";
        String metricType = "myType";
        KafkaZkClient zkClient = KafkaZkClient.apply(zookeeperHost,isSucre,sessionTimeoutMs,
                connectionTimeoutMs,maxInFlightRequests,time,metricGroup,metricType);
        AdminZkClient adminZkClient = new AdminZkClient(zkClient);
        try {
            // run
            LOGGER.info("Executing: ListTopics ");

                return JavaConversions.asJavaCollection(adminZkClient.getAllTopicConfigs().keys())
                        .stream()
                        .collect(Collectors.toList());

        } finally {
            zkClient.close();
        }
    }

    /**
     * Delete all topics
     */
    public void deleteAllTopics() {
        for (String topic : listTopics()) {
            try {
                deleteTopic(topic);
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * Delete a topic.
     *
     * @param topicName The name of the topic to delete
     */
    public void deleteTopic(String topicName) {
        String zookeeperHost = zookeeperString;
        Boolean isSucre = false;
        int sessionTimeoutMs = 200000;
        int connectionTimeoutMs = 15000;
        int maxInFlightRequests = 10;
        Time time = Time.SYSTEM;
        String metricGroup = "myGroup";
        String metricType = "myType";
        KafkaZkClient zkClient = KafkaZkClient.apply(zookeeperHost,isSucre,sessionTimeoutMs,
                connectionTimeoutMs,maxInFlightRequests,time,metricGroup,metricType);
        AdminZkClient adminZkClient = new AdminZkClient(zkClient);
        try {
            // run
            LOGGER.info("Executing: DeleteTopic " + topicName);
            adminZkClient.deleteTopic(topicName);
        } finally {
            zkClient.close();
        }
    }

    public void shutdown() {
        if (broker != null) {
            broker.shutdown();
            broker.awaitShutdown();
        }
        if (zookeeper != null) zookeeper.shutdown();
    }

    public List<Message<String, String>> readAllMessages(final String topicName) {
        return readMessages(topicName, Integer.MAX_VALUE, new StringMessageExtractor());
    }

    public <K, V> List<Message<K, V>> readMessages(final String topicName, final int maxPoll, final MessageExtractor<K, V> messageExtractor) {
        final Properties props = new Properties();
        props.put("bootstrap.servers", brokerString);
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", ByteArrayDeserializer.class.getName());
        props.put("value.deserializer", ByteArrayDeserializer.class.getName());
        props.put("max.poll.records", String.valueOf(maxPoll));
        try (final KafkaConsumer<byte[], byte[]> kafkaConsumer = new KafkaConsumer<>(props)) {
            kafkaConsumer.subscribe(Collections.singletonList(topicName));
            kafkaConsumer.poll(0); // dummy poll
            kafkaConsumer.seekToBeginning(Collections.singletonList(new TopicPartition(topicName, 0)));
            final ConsumerRecords<byte[], byte[]> records = kafkaConsumer.poll(10000);
            final List<Message<K, V>> messages = new ArrayList<>();
            for (ConsumerRecord<byte[], byte[]> record : records) {
                messages.add(messageExtractor.extract(record));
            }
            return messages;
        }
    }

    @SafeVarargs
    public final void sendMessagesAsString(final ProducerRecord<String, String>... records) {
        sendMessages(StringSerializer.class.getName(), StringSerializer.class.getName(), Arrays.asList(records));
    }

    public final <K, V> void sendMessages(String keySerializer, String valueSerializer, final Iterable<ProducerRecord<K, V>> records) {
        KafkaProducer<K, V> producer = createProducer(keySerializer, valueSerializer);
        for (final ProducerRecord<K, V> record : records) {
            try {
                producer.send(record).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            } finally {
                producer.flush();
            }
        }
    }

    /**
     * Set custom broker configuration.
     * See available config keys in the kafka documentation: http://kafka.apache.org/documentation.html#brokerconfigs
     */
    public final void setKafkaBrokerConfig(String configKey, String configValue) {
        kafkaBrokerConfig.setProperty(configKey, configValue);
    }


    public interface MessageExtractor<K, V> {
        Message<K, V> extract(ConsumerRecord<byte[], byte[]> record);
    }

    public class StringMessageExtractor implements MessageExtractor<String, String> {
        @Override
        public Message<String, String> extract(final ConsumerRecord<byte[], byte[]> record) {
            String key = record.key() != null ? new String(record.key()) : null;
            String value = record.value() != null ? new String(record.value()) : null;
            return new Message<>(key, value);
        }
    }

    public static class Message<K, V> {
        private final K key;
        private final V value;


        public Message(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

}