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
    public String key;
    public String value;
    public String timestamp;
    public String timestampETL;
}