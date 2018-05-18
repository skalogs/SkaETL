package io.skalogs.skaetl.admin;

import io.skalogs.skaetl.config.ZookeeperConfiguration;
import io.skalogs.skaetl.domain.TopicInfo;
import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.common.UnknownTopicOrPartitionException;
import kafka.utils.ZkUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
@Slf4j
public class KafkaAdminService {

    private final ZookeeperConfiguration zookeeperConfiguration;


    public void deleteTopic(String topicName) {
        ZkUtils zkUtils = zookeeperConfiguration.newConnection();
        try {
            AdminUtils.deleteTopic(zkUtils, topicName);
            log.info("Delete topic ok {}", topicName);
        } catch (UnknownTopicOrPartitionException e) {
            log.error("an error occured while deleting topic" + topicName, e);
        } catch (RuntimeException e) {
            log.error("Error for delete name {} msg {}", topicName, e);
        } finally {
            zkUtils.close();
        }
    }

    public void deleteTopics(String... topics) {
        Stream.of(topics)
                .filter(e -> StringUtils.isNotBlank(e))
                .forEach(this::deleteTopic);
    }

    public void createTopics(List<TopicInfo> listTopic) {
        listTopic.stream().forEach(this::createTopic);
    }

    public void buildTopic(String... topicList) {
        createTopics(Stream.of(topicList)
                .filter(e -> StringUtils.isNotBlank(e))
                .map(e -> createTopic(e))
                .collect(toList()));
    }

    private TopicInfo createTopic(String item) {
        return TopicInfo.builder()
                .name(item)
                .secure(zookeeperConfiguration.isTopicSecured())
                .retentionHours(zookeeperConfiguration.getTopicDefaultRetentionHours())
                .replica(zookeeperConfiguration.getTopicDefaultReplica())
                .partition(zookeeperConfiguration.getTopicDefaultPartition())
                .build();
    }

    public void createTopic(TopicInfo topicInfo) {

        Properties paramTopic = new Properties();
        Integer retention = topicInfo.getRetentionHours() * 60 * 1000;
        paramTopic.put(TopicConfig.RETENTION_MS_CONFIG, retention.toString());

        ZkUtils zkUtils = zookeeperConfiguration.newConnection();

        try {
            AdminUtils.createTopic(zkUtils, topicInfo.getName(), topicInfo.getPartition(), topicInfo.getReplica(), paramTopic, RackAwareMode.Enforced$.MODULE$);
            log.info("Creation topic at startup ok {}", topicInfo);
        } catch (TopicExistsException e) {
            log.info("Topic {} already exist !", topicInfo.getName());
        } catch (RuntimeException e) {
            log.error("Error for create {} msg {}", topicInfo, e);
        } finally {
            zkUtils.close();
        }
    }
}