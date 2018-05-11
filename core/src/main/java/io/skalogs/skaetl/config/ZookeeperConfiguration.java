package io.skalogs.skaetl.config;

import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import lombok.Getter;
import lombok.Setter;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "zookeeper")
public class ZookeeperConfiguration {
    private String bootstrapServers = "localhost:2181";
    private Integer sessionTimeoutSec = 10;
    private Integer connectionTimeoutSec = 8;
    private Integer topicDefaultReplica= 1;
    private Integer topicDefaultPartition = 2;
    private boolean topicSecured=false;
    private Integer topicDefaultRetentionHours = 8;

    public ZkUtils newConnection() {
        int sessionTimeoutMs = sessionTimeoutSec * 1000;
        int connectionTimeoutMs = connectionTimeoutSec * 1000;
        String zookeeperConnect = bootstrapServers;

        ZkClient zkClient = new ZkClient(
                zookeeperConnect,
                sessionTimeoutMs,
                connectionTimeoutMs,
                ZKStringSerializer$.MODULE$);
        return new ZkUtils(zkClient, new ZkConnection(zookeeperConnect), topicSecured);
    }
}
