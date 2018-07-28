package io.skalogs.skaetl.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Streams;
import io.skalogs.skaetl.domain.MetadataItem;
import io.skalogs.skaetl.domain.ProcessReferential;
import io.skalogs.skaetl.domain.Referential;
import io.skalogs.skaetl.domain.TypeReferential;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueStore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
public class ReferentialTransformer extends AbstractValueTransformer<JsonNode, List<Referential>> {

    public static final String REFERENTIAL = "referential";
    private final ProcessReferential processReferential;
    private KeyValueStore<String, Referential> referentialStateStore;
    private final JSONUtils jsonUtils = JSONUtils.getInstance();

    public ReferentialTransformer(ProcessReferential processReferential) {
        this.processReferential = processReferential;
    }

    @Override
    public void init(ProcessorContext context) {
        super.init(context);
        referentialStateStore = (KeyValueStore<String, Referential>) context.getStateStore(REFERENTIAL);

        context.schedule(5 * 60 * 1000, PunctuationType.WALL_CLOCK_TIME, (timestamp) -> flush());
    }

    @Override
    public List<Referential> transform(JsonNode jsonNode) {
        return save(processReferential.getListAssociatedKeys().stream()
                .filter(keyTrack -> jsonUtils.has(keyTrack,jsonNode))
                .filter(keyTrack -> !jsonUtils.at(keyTrack,jsonNode).asText().equals("null"))
                .map(keyTrack -> createReferential(keyTrack, jsonNode))
                .collect(toList()));
    }

    private Referential createReferential(String keyTrack, JsonNode jsonNode) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Referential ref = Referential.builder()
                .key(processReferential.getReferentialKey())
                .value(jsonUtils.at(keyTrack,jsonNode).asText())
                .timestamp(jsonNode.path("timestamp").asText())
                .metadataItemSet(buildMetadata(jsonNode))
                .idProcessReferential(processReferential.getIdProcess())
                .nameProcessReferential(processReferential.getName())
                .project("REFERENTIAL")
                .type(processReferential.getName())
                .timestampETL(df.format(new Date()))
                .creationDate(df.format(new Date()))
                .build();
        return ref;
    }

    private Set<MetadataItem> buildMetadata(JsonNode jsonNode) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return processReferential.getListMetadata().stream()
                .filter(metadata -> jsonUtils.has(metadata,jsonNode))
                .filter(metadata -> !jsonUtils.at(metadata,jsonNode).asText().equals("null"))
                .map(metadata -> MetadataItem.builder()
                        .key(metadata)
                        .value(jsonUtils.at(metadata,jsonNode).asText())
                        .timestamp(jsonNode.path("timestamp").asText())
                        .timestampETL(df.format(new Date()))
                        .creationDate(df.format(new Date()))
                        .build())
                .collect(Collectors.toCollection(HashSet::new));
    }

    public void flush() {
        List<Referential> referentials = Streams.stream(referentialStateStore.all()).map(entry -> entry.value).filter(entry -> entry != null).collect(Collectors.toList());
        log.info("{} Persist Referential size {}", getContext().applicationId(), referentials.size());
        referentials.stream()
                .forEach(referential -> computeValidation(referential));
    }

    private void computeValidation(Referential referential) {
        validationTime(referential, new ArrayList<>());
    }

    public List<Referential> save(List<Referential> referentialList) {
        return referentialList
                .stream()
                .map(item -> compute(item))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<Referential> compute(Referential newReferential) {
        String key = newReferential.getIdProcessReferential() + "#" + newReferential.getKey() + "#" + newReferential.getValue();
        List<Referential> referentials = new ArrayList<>();
        Referential oldReferential = referentialStateStore.get(key);
        if (oldReferential == null) {
            referentials.add(newReferential);
            referentialStateStore.put(key, newReferential);
        } else {
            //we must validate before update
            validationTime(oldReferential, referentials);
            //we must notificate before update
            tracking(oldReferential, newReferential, referentials);
            Referential value = mergeMetadata(oldReferential
                            .withValue(newReferential.getValue())
                            .withTimestamp(newReferential.getTimestamp())
                            .withNbChange(oldReferential.getNbChange() + 1)
                    , newReferential.getMetadataItemSet());
            referentials.add(value);
            referentialStateStore.put(key, value);
        }
        return referentials;
    }

    private void validationTime(Referential referential, List<Referential> referentials) {
        if (processReferential.getIsValidationTimeAllField()) {
            validationTimeAllField(referential, referentials);
        } else if (processReferential.getIsValidationTimeField()) {
            validationTimeField(referential, referentials);
        }
    }

    private void validationTimeAllField(Referential referential, List<Referential> referentials) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        long diffInSec = differenceTime(referential.getTimestamp(), df.format(new Date()));
        log.debug("referential {} validationTimeAllField old : {} new: {} diff {}", referential.getKey() + "---" + referential.getValue(), referential.getTimestampETL(), df.format(new Date()), diffInSec);
        if (diffInSec > processReferential.getTimeValidationAllFieldInSec()) {
            referentials.add(referential
                    .withType(referential.getType() + "-validation")
                    .withTypeReferential(TypeReferential.VALIDATION)
                    .withTimeExceeded(diffInSec)
                    .withTimeValidationAllFieldInSec(processReferential.getTimeValidationAllFieldInSec())
            );

        }
    }

    private void validationTimeField(Referential referential, List<Referential> referentials) {
        MetadataItem item = getItem(processReferential.getFieldChangeValidation(), referential.getMetadataItemSet());
        if (item != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            long diffInSec = differenceTime(item.getTimestamp(), df.format(new Date()));
            log.debug("referential {}  validationTimeField old : {} new: {} diff {}", referential.getKey() + "---" + referential.getValue(), item.getTimestampETL(), df.format(new Date()), diffInSec);
            if (diffInSec > processReferential.getTimeValidationFieldInSec()) {
                referentials.add(referential
                        .withType(referential.getType() + "-validation")
                        .withTypeReferential(TypeReferential.VALIDATION)
                        .withTimeExceeded(diffInSec)
                        .withFieldChangeValidation(processReferential.getFieldChangeValidation())
                        .withTimeValidationFieldInSec(processReferential.getTimeValidationFieldInSec())
                );
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
        if (referential.getMetadataItemSet() != null && !referential.getMetadataItemSet().isEmpty()) {
            for (MetadataItem itemRef : referential.getMetadataItemSet()) {
                if (itemRef.getKey().equals(itemNew.getKey())) {
                    itemRef.setValue(itemNew.getValue());
                    itemRef.setTimestamp(referential.getTimestamp());
                    itemRef.setTimestampETL(df.format(new Date()));
                    noTreat = false;
                }
            }
            if (noTreat) {
                referential.getMetadataItemSet().add(itemNew);
            }
        }
    }

    private void tracking(Referential referential, Referential referentialNew, List<Referential> referentials) {
        if (processReferential.getIsNotificationChange()) {
            MetadataItem itemOld = getItem(processReferential.getFieldChangeNotification(), referential.getMetadataItemSet());
            MetadataItem itemNew = getItem(processReferential.getFieldChangeNotification(), referentialNew.getMetadataItemSet());
            if (itemNew != null && itemOld == null) {
                referentials.add(trackChange(referential, itemNew.getTimestamp(), new Long(0L), processReferential.getFieldChangeNotification(), itemNew.getValue()));
            } else if (itemNew != null && !itemOld.getValue().equals(itemNew.getValue())) {
                referentials.add(trackChange(referential, itemNew.getTimestamp(), Long.valueOf(differenceTime(itemOld.getTimestamp(), itemNew.getTimestamp())), processReferential.getFieldChangeNotification(), itemNew.getValue()));
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

    private Referential trackChange(Referential referential, String newTimeStamp, Long timeBetweenEventSec, String keyMetadata, String newMetadataValue) {
        return referential
                .withType(referential.getType() + "-tracking")
                .withTypeReferential(TypeReferential.TRACKING)
                .withNewTimestamp(newTimeStamp)
                .withKeyMetadataModified(keyMetadata)
                .withNewMetadataValue(newMetadataValue)
                .withTimeBetweenEventSec(timeBetweenEventSec);
    }

    private MetadataItem getItem(String field, Set<MetadataItem> metadataItemSet) {
        if (metadataItemSet != null && !metadataItemSet.isEmpty()) {
            for (MetadataItem item : metadataItemSet) {
                if (item.getKey().equals(field)) {
                    return item;
                }
            }
        }
        return null;
    }

}
