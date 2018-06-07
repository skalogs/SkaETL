package io.skalogs.skaetl.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Streams;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.MetadataItem;
import io.skalogs.skaetl.domain.ProcessReferential;
import io.skalogs.skaetl.domain.Referential;
import io.skalogs.skaetl.serdes.JsonNodeSerialializer;
import io.skalogs.skaetl.utils.JSONUtils;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueStore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.skalogs.skaetl.service.referential.ReferentialESService.*;
import static java.util.stream.Collectors.toList;

@Slf4j
public class ReferentialProcessor extends AbstractProcessor<String, JsonNode> implements ReferentialService {

    public static final String REFERENTIAL = "referential";
    private final ProcessReferential processReferential;
    private final Producer<String, JsonNode> referentialProducer;
    private KeyValueStore<String, Referential> referentialStateStore;

    public ReferentialProcessor(ProcessReferential processReferential, KafkaConfiguration kafkaConfiguration) {
        this.processReferential = processReferential;
        this.referentialProducer = KafkaUtils.kafkaProducer(kafkaConfiguration.getBootstrapServers(), StringSerializer.class, JsonNodeSerialializer.class);
    }

    @Override
    public void init(ProcessorContext context) {
        super.init(context);
        referentialStateStore = (KeyValueStore<String, Referential>) context.getStateStore(REFERENTIAL);

        context.schedule(5 * 60 * 1000, PunctuationType.WALL_CLOCK_TIME, (timestamp) -> flush());
    }

    @Override
    public void process(String key, JsonNode jsonNode) {
        save(processReferential, processReferential.getListAssociatedKeys().stream()
                .filter(keyTrack -> jsonNode.has(keyTrack))
                .filter(keyTrack -> !jsonNode.get(keyTrack).asText().equals("null"))
                .map(keyTrack -> createReferential(keyTrack, jsonNode))
                .collect(toList()));
    }

    private Referential createReferential(String keyTrack, JsonNode jsonNode) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Referential ref = Referential.builder()
                .key(processReferential.getReferentialKey())
                .value(jsonNode.path(keyTrack).asText())
                .timestamp(jsonNode.path("timestamp").asText())
                .metadataItemSet(buildMetadata(jsonNode))
                .idProcessReferential(processReferential.getIdProcess())
                .nameProcessReferential(processReferential.getName())
                .project("REFERENTIAL")
                .type(processReferential.getName())
                .timestampETL(df.format(new Date()))
                .build();
        return ref;
    }

    private Set<MetadataItem> buildMetadata(JsonNode jsonNode) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return processReferential.getListMetadata().stream()
                .filter(metadata -> jsonNode.has(metadata))
                .filter(metadata -> !jsonNode.get(metadata).asText().equals("null"))
                .map(metadata -> MetadataItem.builder()
                        .key(metadata)
                        .value(jsonNode.path(metadata).asText())
                        .timestamp(jsonNode.path("timestamp").asText())
                        .timestampETL(df.format(new Date()))
                        .build())
                .collect(Collectors.toCollection(HashSet::new));
    }

    public void flush() {
        List<Referential> referentials = Streams.stream(referentialStateStore.all()).map(entry -> entry.value).filter(entry -> entry != null) .collect(Collectors.toList());
        log.info("{} Persist Referential size {}", context().applicationId(), referentials.size());
        referentials.stream()
                .forEach(referential -> referentialToKafka(referential));
    }

    private void referentialToKafka(Referential referential) {
        validationTime(processReferential, referential);
        referentialProducer.send(new ProducerRecord<>(TOPIC_REFERENTIAL_ES, JSONUtils.getInstance().toJsonNode(referential)));
    }

    public void save(ProcessReferential processReferential, List<Referential> referentialList) {
        referentialList.stream().forEach(item -> compute(processReferential, item));
    }

    public void compute(ProcessReferential processReferential, Referential newReferential) {
        String key = newReferential.getIdProcessReferential() + "#" + newReferential.getKey() + "#" + newReferential.getValue();
        Referential ref = referentialStateStore.get(key);
        if (ref == null) {
            referentialStateStore.put(key, newReferential);
        } else {
            //we must validate before update
            validationTime(processReferential,ref);
            //we must notificate before update
            notification(processReferential, ref, newReferential);
            referentialStateStore.put(key,
                    mergeMetadata(ref.withValue(newReferential.getValue()).withTimestamp(newReferential.getTimestamp()), newReferential.getMetadataItemSet()));
        }
    }

    private void validationTime(ProcessReferential processReferential, Referential referential) {
        if (processReferential.getIsValidationTimeAllField()) {
            validationTimeAllField(processReferential, referential);
        } else if (processReferential.getIsValidationTimeField()) {
            validationTimeField(processReferential, referential);
        }
    }

    private void validationTimeAllField(ProcessReferential processReferential, Referential referential) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        long diffInSec = differenceTime(referential.getTimestampETL(), df.format(new Date()));
        log.debug("referential {} validationTimeAllField old : {} new: {} diff {}",referential.getKey()+"---"+referential.getValue(),referential.getTimestampETL(),df.format(new Date()),diffInSec);
        if (diffInSec > processReferential.getTimeValidationAllFieldInSec()) {
            ObjectNode jsonNode = (ObjectNode) JSONUtils.getInstance().toJsonNode(referential);
            jsonNode.put("typeReferential", "validation");
            jsonNode.put("timeExceeded", diffInSec);
            jsonNode.put("timeValidationAllFieldInSec", processReferential.getTimeValidationAllFieldInSec());
            referentialProducer.send(new ProducerRecord<>(TOPIC_REFERENTIAL_VALIDATION_ES, jsonNode));
        }
    }

    private void validationTimeField(ProcessReferential processReferential, Referential referential) {
        MetadataItem item = getItem(processReferential.getFieldChangeValidation(), referential.getMetadataItemSet());
        if (item != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            long diffInSec = differenceTime(item.getTimestampETL(), df.format(new Date()));
            log.debug("referential {}  validationTimeField old : {} new: {} diff {}",referential.getKey()+"---"+referential.getValue(),item.getTimestampETL(),df.format(new Date()),diffInSec);
            if (diffInSec > processReferential.getTimeValidationFieldInSec()) {
                ObjectNode jsonNode = (ObjectNode) JSONUtils.getInstance().toJsonNode(referential);
                jsonNode.put("typeReferential", "validation");
                jsonNode.put("timeExceeded", diffInSec);
                jsonNode.put("fieldChangeValidation", processReferential.getFieldChangeValidation());
                jsonNode.put("timeValidationFieldInSec", processReferential.getTimeValidationFieldInSec());
                referentialProducer.send(new ProducerRecord<>(TOPIC_REFERENTIAL_VALIDATION_ES, jsonNode));
            }
        }
    }

    private Referential mergeMetadata(Referential referential, Set<MetadataItem> newMetadataItemSet) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        referential.setTimestampETL(df.format(new Date()));
        newMetadataItemSet.stream().forEach(itemNew -> updateRefMetadata(referential, itemNew));
        return referential;
    }

    private void updateRefMetadata(Referential referential, MetadataItem itemNew) {
        Boolean noTreat = true;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        if(referential.getMetadataItemSet() !=null && !referential.getMetadataItemSet().isEmpty()) {
            for (MetadataItem itemRef : referential.getMetadataItemSet()) {
                if (itemRef.getKey().equals(itemNew.getKey())) {
                    itemRef.setValue(itemNew.getValue());
                    itemRef.setTimestamp(referential.getTimestamp());
                    itemRef.setTimestampETL(df.format(new Date()));
                    noTreat = false;
                }
            }
        }
        if (noTreat) {
            referential.getMetadataItemSet().add(itemNew);
        }
    }

    private void notification(ProcessReferential processReferential, Referential referential, Referential referentialNew) {
        if (processReferential.getIsNotificationChange()) {
            MetadataItem itemOld = getItem(processReferential.getFieldChangeNotification(), referential.getMetadataItemSet());
            MetadataItem itemNew = getItem(processReferential.getFieldChangeNotification(), referentialNew.getMetadataItemSet());
            if (itemNew != null && itemOld == null) {
                notificationReferentialToKafka(referential, itemNew.getTimestamp(), new Long(0L), processReferential.getFieldChangeNotification(), itemNew.getValue());
            } else if (itemNew != null && !itemOld.getValue().equals(itemNew.getValue())) {
                notificationReferentialToKafka(referential, itemNew.getTimestamp(), Long.valueOf(differenceTime(itemOld.getTimestamp(), itemNew.getTimestamp())), processReferential.getFieldChangeNotification(), itemNew.getValue());
            }
        }
    }

    private long differenceTime(String oldTimestamp, String newTimestamp) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        long diffInSeconds = 0;
        try {
            Date dateOld = df.parse(oldTimestamp);
            Date dateNew = df.parse(newTimestamp);
            long diffInMs = dateNew.getTime() - dateOld.getTime();
            if (diffInMs > 0) {
                diffInSeconds = TimeUnit.SECONDS.convert(diffInMs, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            log.error("Error in diff between old {} and new {}", oldTimestamp, newTimestamp);
        }
        return diffInSeconds;
    }

    private void notificationReferentialToKafka(Referential referential, String newTimeStamp, Long timeBetweenEventSec, String keyMetadata, String newMetadataValue) {
        ObjectNode jsonNode = (ObjectNode) JSONUtils.getInstance().toJsonNode(referential);
        jsonNode.put("typeReferential", "notification");
        jsonNode.put("newTimeStamp", newTimeStamp);
        jsonNode.put("keyMetadataModified", keyMetadata);
        jsonNode.put("newMetadataValue", newMetadataValue);
        jsonNode.put("timeBetweenEventSec", timeBetweenEventSec);
        referentialProducer.send(new ProducerRecord<>(TOPIC_REFERENTIAL_NOTIFICATION_ES, jsonNode));
    }

    private MetadataItem getItem(String field, Set<MetadataItem> metadataItemSet) {
        if(metadataItemSet !=null && !metadataItemSet.isEmpty()) {
            for (MetadataItem item : metadataItemSet) {
                if (item.getKey().equals(field)) {
                    return item;
                }
            }
        }
        return null;
    }

}
