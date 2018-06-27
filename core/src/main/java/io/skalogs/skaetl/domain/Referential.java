package io.skalogs.skaetl.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Wither;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Wither
public class Referential {
    private String idProcessReferential;
    private String nameProcessReferential;
    private String key;
    private String value;
    private String timestamp;
    private String project;
    private String type;
    @Builder.Default
    private RetentionLevel retentionLevel = RetentionLevel.year;
    @JsonProperty("metadataItemSet_nested")
    private Set<MetadataItem> metadataItemSet = new HashSet<MetadataItem>();
    private String timestampETL;
    private String creationDate;

    //tracking & validation
    private TypeReferential typeReferential;
    private String newTimestamp;

    //tracking
    @Builder.Default
    private Long nbChange = 0L;
    private String keyMetadataModified;
    private String newMetadataValue;
    private Long timeBetweenEventSec;


    //validation
    private Long timeExceeded;
    private Integer timeValidationAllFieldInSec;
    private String fieldChangeValidation;
    private Integer timeValidationFieldInSec;
}