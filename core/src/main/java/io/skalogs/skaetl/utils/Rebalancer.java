package io.skalogs.skaetl.utils;

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

import com.google.common.collect.Lists;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

@Slf4j
public class Rebalancer implements ConsumerRebalanceListener {

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        log.info("On Partition revoked");
        for (TopicPartition partition : partitions) {
            Metrics.counter("skaetl_nb_partition_revoked_count",
                    Lists.newArrayList(
                            Tag.of("topic",partition.topic()),
                            Tag.of("partition",String.valueOf(partition.partition()))
                    )
            ).increment();
        }
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        log.info("On Partition assigned");
        for (TopicPartition partition : partitions) {
            Metrics.counter("skaetl_nb_partition_assigned_count",
                    Lists.newArrayList(
                            Tag.of("topic",partition.topic()),
                            Tag.of("partition",String.valueOf(partition.partition()))
                    )
            ).increment();
        }
    }
}
