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
    public String idProcessReferential;
    public String nameProcessReferential;
    public String key;
    public String value;
    public String timestamp;
    public String project;
    public String type;
    @Builder.Default
    public RetentionLevel retentionLevel = RetentionLevel.year;
    @JsonProperty("metadataItemSet_nested")
    public Set<MetadataItem> metadataItemSet = new HashSet<MetadataItem>();
    public String timestampETL;
}