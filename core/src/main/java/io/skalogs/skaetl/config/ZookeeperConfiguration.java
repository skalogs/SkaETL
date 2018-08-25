package io.skalogs.skaetl.config;

/*-
 * #%L
 * core
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
