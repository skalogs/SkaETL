package io.skalogs.skaetl.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Wither;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Wither
public class GrokData {
    @JsonIgnore
    public Long id;
    public String key;
    public String value;
}
