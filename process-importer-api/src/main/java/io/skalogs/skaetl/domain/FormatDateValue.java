package io.skalogs.skaetl.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class FormatDateValue {
    public String keyField;
    public String srcFormat;
    public String targetFormat;
}
