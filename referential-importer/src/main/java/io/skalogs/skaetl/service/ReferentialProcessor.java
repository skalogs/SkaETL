package io.skalogs.skaetl.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.MetadataItem;
import io.skalogs.skaetl.domain.ProcessReferential;
import io.skalogs.skaetl.domain.Referential;
import io.skalogs.skaetl.service.referential.ReferentialESService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.AbstractProcessor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
public class ReferentialProcessor extends AbstractProcessor<String, JsonNode> {

    private final ProcessReferential processReferential;
    private final ReferentialESService referentialESService;

    public ReferentialProcessor(ProcessReferential processReferential, ReferentialESService referentialESService) {
        this.processReferential = processReferential;
        this.referentialESService = referentialESService;
    }

    @Override
    public void process(String key, JsonNode jsonNode) {
        referentialESService.save(processReferential.getListAssociatedKeys().stream()
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
        return processReferential.getListMetadata().stream()
                .filter(metadata -> jsonNode.has(metadata))
                .filter(metadata -> !jsonNode.get(metadata).asText().equals("null"))
                .map(metadata -> MetadataItem.builder()
                        .key(metadata)
                        .value(jsonNode.path(metadata).asText())
                        .build())
                .collect(Collectors.toCollection(HashSet::new));
    }

}
