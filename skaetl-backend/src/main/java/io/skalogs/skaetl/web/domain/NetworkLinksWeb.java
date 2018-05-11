package io.skalogs.skaetl.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NetworkLinksWeb {
    private String sid;
    private String tid;
    @JsonProperty("_color")
    private String color;
    private String name;
}
