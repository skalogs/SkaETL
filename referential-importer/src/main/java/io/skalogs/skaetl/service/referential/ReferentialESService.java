package io.skalogs.skaetl.service.referential;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.serdes.GenericSerdes;
import io.skalogs.skaetl.service.processor.ReferentialElasticsearchProcessor;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class ReferentialESService {

    private final KafkaAdminService kafkaAdminService;
    private final ApplicationContext applicationContext;
    private final String bootstrapServer;
    public static String TOPIC_REFERENTIAL_ES = "topicReferentialEs";
    public static String TOPIC_REFERENTIAL_NOTIFICATION_ES = "topicReferentialNotification";
    public static String TOPIC_REFERENTIAL_VALIDATION_ES = "topicReferentialValidation";

    public ReferentialESService(KafkaConfiguration kafkaConfiguration, KafkaAdminService kafkaAdminService, ApplicationContext applicationContext) {
        this.bootstrapServer = kafkaConfiguration.getBootstrapServers();
        this.kafkaAdminService = kafkaAdminService;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    private void createStreamEs() {
        createStreamReferential(TOPIC_REFERENTIAL_ES,"REFERENTIAL#ELASTICSEARCH");
        createStreamReferential(TOPIC_REFERENTIAL_NOTIFICATION_ES,"REFERENTIAL-NOTIFICATION#ELASTICSEARCH");
        createStreamReferential(TOPIC_REFERENTIAL_VALIDATION_ES,"REFERENTIAL-VALIDATION#ELASTICSEARCH");
    }

    private void createStreamReferential(String topic, String nameStream) {
        kafkaAdminService.buildTopic(topic);
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, JsonNode> streamToES = builder.stream(topic, Consumed.with(Serdes.String(), GenericSerdes.jsonNodeSerde()));
        streamToES.process(() -> applicationContext.getBean(ReferentialElasticsearchProcessor.class));
        KafkaStreams streams = new KafkaStreams(builder.build(), KafkaUtils.createKStreamProperties(nameStream, bootstrapServer));
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }
}
