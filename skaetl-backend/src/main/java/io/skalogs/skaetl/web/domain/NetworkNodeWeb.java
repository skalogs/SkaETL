package io.skalogs.skaetl.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NetworkNodeWeb {
    private String id;
    private String name;
    @JsonProperty("_color")
    private String color;
}
