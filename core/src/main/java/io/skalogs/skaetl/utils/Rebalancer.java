package io.skalogs.skaetl.utils;

import io.prometheus.client.Counter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

@Slf4j
public class Rebalancer implements ConsumerRebalanceListener {

    private static final Counter nbPartitionRevokedCount = Counter.build()
            .name("nb_partition_revoked_count")
            .labelNames("topic", "partition")
            .help("nb partition revoked count.")
            .register();

    private static final Counter nbPartitionAssignedCount = Counter.build()
            .name("nb_partition_assigned_count")
            .labelNames("topic", "partition")
            .help("nb partition assigned count.")
            .register();

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        log.info("On Partition revoked");
        for (TopicPartition partition : partitions) {
            nbPartitionRevokedCount.labels(partition.topic(), "" + partition.partition()).inc();
        }
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        log.info("On Partition assigned");
        for (TopicPartition partition : partitions) {
            nbPartitionAssignedCount.labels(partition.topic(), "" + partition.partition()).inc();
        }
    }
}
