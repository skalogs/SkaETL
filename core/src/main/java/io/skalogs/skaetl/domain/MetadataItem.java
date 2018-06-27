package io.skalogs.skaetl.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MetadataItem {
    private String key;
    private String value;
    private String timestamp;
    private String timestampETL;
    private String creationDate;
}