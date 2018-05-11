package io.skalogs.skaetl.domain;

import lombok.*;
import org.springframework.http.HttpMethod;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ExternalHTTPData {
    private String url;
    private Integer refresh;
    private HttpMethod httpMethod;
    private String body;
}
