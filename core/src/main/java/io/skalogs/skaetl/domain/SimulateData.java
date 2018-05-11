package io.skalogs.skaetl.domain;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class SimulateData {
    public String value;
    public JsonNode jsonValue;
    public String message;
}