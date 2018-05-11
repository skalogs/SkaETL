package io.skalogs.skaetl.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GrokDomain {
    public String keyPattern;
    public String valuePattern;
    public String type;
    @JsonProperty("@timestamp")
    public String timestamp;
    public Boolean active;
}
