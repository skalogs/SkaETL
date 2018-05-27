package io.skalogs.skaetl.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.MetadataItem;
import io.skalogs.skaetl.domain.ProcessReferential;
import io.skalogs.skaetl.domain.Referential;
import io.skalogs.skaetl.serdes.JsonNodeSerialializer;
import io.skalogs.skaetl.utils.JSONUtils;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.processor.AbstractProcessor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.skalogs.skaetl.service.referential.ReferentialESService.*;
import static java.util.stream.Collectors.toList;

@Slf4j
public class ReferentialProcessor extends AbstractProcessor<String, JsonNode> implements ReferentialService{

    private final ProcessReferential processReferential;
    private final Producer<String, JsonNode> referentialProducer;
    private Map<String, Referential> referentialMap = new HashMap<>();

    public ReferentialProcessor(ProcessReferential processReferential,KafkaConfiguration kafkaConfiguration) {
        this.processReferential = processReferential;
        this.referentialProducer = KafkaUtils.kafkaProducer(kafkaConfiguration.getBootstrapServers(), StringSerializer.class, JsonNodeSerialializer.class);
    }

    @Override
    public void process(String key, JsonNode jsonNode) {
        save(processReferential,processReferential.getListAssociatedKeys().stream()
                .filter(keyTrack -> jsonNode.has(keyTrack))
                .filter(keyTrack -> !jsonNode.get(keyTrack).asText().equals("null"))
                .map(keyTrack -> createReferential(keyTrack, jsonNode))
                .collect(toList()));
    }

    private Referential createReferential(String keyTrack, JsonNode jsonNode) {
        Referential ref = Referential.builder()
                .key(processReferential.getReferentialKey())
                .value(jsonNode.path(keyTrack).asText())
                .timestamp(jsonNode.path("timestamp").asText())
                .metadataItemSet(buildMetadata(jsonNode))
                .idProcessReferential(processReferential.getIdProcess())
                .nameProcessReferential(processReferential.getName())
                .project("REFERENTIAL")
                .type(processReferential.getName())
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
                        .timestamp(df.format(new Date()))
                        .build())
                .collect(Collectors.toCollection(HashSet::new));
    }

    public void flush() {
        synchronized (this) {
            log.info("Persist Referential size {}", referentialMap.values().size());
            referentialMap.values().stream()
                    .forEach(referential -> referentialToKafka(referential));
        }
    }

    private void referentialToKafka(Referential referential) {
        validationTime(processReferential, referential);
        referentialProducer.send(new ProducerRecord<>(TOPIC_REFERENTIAL_ES, JSONUtils.getInstance().toJsonNode(referential)));
    }

    public void save(ProcessReferential processReferential, List<Referential> referentialList) {
        referentialList.stream().forEach(item -> compute(processReferential, item));
    }

    public void compute(ProcessReferential processReferential, Referential newReferential) {
        Referential ref = referentialMap.get(newReferential.getIdProcessReferential() + "#" + newReferential.getKey() + "#" + newReferential.getValue());
        if (ref == null) {
            referentialMap.put(newReferential.getIdProcessReferential() + "#" + newReferential.getKey() + "#" + newReferential.getValue(), newReferential);
        } else {
            notification(processReferential, ref, newReferential);
            referentialMap.put(newReferential.getIdProcessReferential() + "#" + newReferential.getKey() + "#" + newReferential.getValue(),
                    mergeMetadata(ref.withValue(newReferential.getValue()).withTimestamp(newReferential.getTimestamp()), newReferential.getMetadataItemSet()));
        }
    }

    private void validationTime(ProcessReferential processReferential, Referential referential) {
        if (processReferential.getIsValidationTimeAllField()) {
            validationTimeAllField(processReferential,referential);
        }else if (processReferential.getIsValidationTimeField()) {
            validationTimeField(processReferential,referential);
        }
    }

    private void validationTimeAllField(ProcessReferential processReferential, Referential referential){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        long diffInSec = differenceTime(referential.getTimestamp(),df.format(new Date()));
        if(diffInSec>processReferential.getTimeValidationInSec()){
            ObjectNode jsonNode = (ObjectNode) JSONUtils.getInstance().toJsonNode(referential);
            jsonNode.put("timeExceeded",diffInSec);
            jsonNode.put("timeValidationInSec",processReferential.getTimeValidationInSec());
            referentialProducer.send(new ProducerRecord<>(TOPIC_REFERENTIAL_VALIDATION_ES, jsonNode));
        }
    }

    private void validationTimeField(ProcessReferential processReferential, Referential referential){
        MetadataItem item = getItem(processReferential.getFieldChangeValidation(), referential.getMetadataItemSet());
        if(item !=null && StringUtils.isNotBlank(item.getTimestamp())) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            long diffInSec = differenceTime(item.getTimestamp(), df.format(new Date()));
            if (diffInSec > processReferential.getTimeValidationInSec()) {
                ObjectNode jsonNode = (ObjectNode) JSONUtils.getInstance().toJsonNode(referential);
                jsonNode.put("timeExceeded", diffInSec);
                jsonNode.put("fieldChangeValidation", processReferential.getFieldChangeValidation());
                jsonNode.put("timeValidationInSec", processReferential.getTimeValidationInSec());
                referentialProducer.send(new ProducerRecord<>(TOPIC_REFERENTIAL_VALIDATION_ES, jsonNode));
            }
        }
    }

    private Referential mergeMetadata(Referential referential, Set<MetadataItem> newMetadataItemSet) {
        newMetadataItemSet.stream().forEach(itemNew -> updateRefMetadata(referential, itemNew));
        return referential;
    }

    private void updateRefMetadata(Referential referential, MetadataItem itemNew) {
        Boolean noTreat = true;
        for (MetadataItem itemRef : referential.getMetadataItemSet()) {
            if (itemRef.getKey().equals(itemNew.getKey())) {
                itemRef.setValue(itemNew.getValue());
                noTreat = false;
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
                notificationReferentialToKafka(referential, referentialNew.getTimestamp(), "0", processReferential.getFieldChangeNotification(), itemNew.getValue());
            } else if (itemNew != null && !itemOld.getValue().equals(itemNew.getValue())) {
                notificationReferentialToKafka(referential, referentialNew.getTimestamp(), String.valueOf(differenceTime(referential.getTimestamp(),referentialNew.getTimestamp())), processReferential.getFieldChangeNotification(), itemNew.getValue());
            }
        }
    }

    private long differenceTime(String oldTimestamp, String newTimestamp){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        long diffInSeconds = 0;
        try {
            Date dateOld = df.parse(oldTimestamp);
            Date dateNew = df.parse(newTimestamp);
            diffInSeconds = TimeUnit.SECONDS.convert(Math.abs(dateNew.getTime() - dateOld.getTime()),TimeUnit.MILLISECONDS);
        }catch (Exception e){
            log.error("Error in diff between old {} and new {}",oldTimestamp,newTimestamp);
        }
        return diffInSeconds;
    }

    private void notificationReferentialToKafka(Referential referential, String newTimeStamp, String timeBetweenEventSec, String keyMetadata, String newMetadataValue) {
        ObjectNode jsonNode = (ObjectNode) JSONUtils.getInstance().toJsonNode(referential);
        jsonNode.put("newTimeStamp",newTimeStamp);
        jsonNode.put("keyMetadataModified",keyMetadata);
        jsonNode.put("newMetadataValue",newMetadataValue);
        jsonNode.put("timeBetweenEventSec",timeBetweenEventSec);
        referentialProducer.send(new ProducerRecord<>(TOPIC_REFERENTIAL_NOTIFICATION_ES, jsonNode));
    }

    private MetadataItem getItem(String field, Set<MetadataItem> metadataItemSet) {
        for (MetadataItem item : metadataItemSet) {
            if (item.getKey().equals(field)) {
                return item;
            }
        }
        return null;
    }

}
