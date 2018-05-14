package io.skalogs.skaetl.kafka;

import kafka.admin.TopicCommand;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;
import kafka.utils.ZkUtils;
import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.security.JaasUtils;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Console;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutionException;

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

        String[] arguments = new String[9];
        arguments[0] = "--create";
        arguments[1] = "--zookeeper";
        arguments[2] = zookeeperString;
        arguments[3] = "--replication-factor";
        arguments[4] = "1";
        arguments[5] = "--partitions";
        arguments[6] = String.valueOf(numPartitions);
        arguments[7] = "--topic";
        arguments[8] = topicName;
        TopicCommand.TopicCommandOptions opts = new TopicCommand.TopicCommandOptions(arguments);

        ZkUtils zkUtils = ZkUtils.apply(opts.options().valueOf(opts.zkConnectOpt()),
                30000, 30000, JaasUtils.isZkSecurityEnabled());
        try {
            // run
            LOGGER.info("Executing: CreateTopic " + Arrays.toString(arguments));
            TopicCommand.createTopic(zkUtils, opts);
        } finally {
            zkUtils.close();
        }

    }

    /**
     * @return All topic names
     */
    public List<String> listTopics() {
        String[] arguments = new String[3];
        arguments[0] = "--zookeeper";
        arguments[1] = zookeeperString;
        arguments[2] = "--list";
        TopicCommand.TopicCommandOptions opts = new TopicCommand.TopicCommandOptions(arguments);

        ZkUtils zkUtils = ZkUtils.apply(opts.options().valueOf(opts.zkConnectOpt()),
                30000, 30000, JaasUtils.isZkSecurityEnabled());
        final List<String> topics = new ArrayList<>();
        try {
            // run
            LOGGER.info("Executing: ListTopics " + Arrays.toString(arguments));

            PrintStream oldOut = Console.out();
            try {
                Console.setOut(new PrintStream(oldOut) {
                    @Override
                    public void print(String s) {
                        super.print(s);
                        if (!s.endsWith("marked for deletion")) {
                            topics.add(s);
                        }
                    }
                });
                TopicCommand.listTopics(zkUtils, opts);
            } finally {
                Console.setOut(oldOut);
            }
        } finally {
            zkUtils.close();
        }

        return topics;
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
        String[] arguments = new String[5];
        arguments[0] = "--zookeeper";
        arguments[1] = zookeeperString;
        arguments[2] = "--delete";
        arguments[3] = "--topic";
        arguments[4] = topicName;
        TopicCommand.TopicCommandOptions opts = new TopicCommand.TopicCommandOptions(arguments);

        ZkUtils zkUtils = ZkUtils.apply(opts.options().valueOf(opts.zkConnectOpt()),
                30000, 30000, JaasUtils.isZkSecurityEnabled());
        try {
            // run
            LOGGER.info("Executing: DeleteTopic " + Arrays.toString(arguments));
            TopicCommand.deleteTopic(zkUtils, opts);
        } finally {
            zkUtils.close();
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