package io.skalogs.skaetl.utils;

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
