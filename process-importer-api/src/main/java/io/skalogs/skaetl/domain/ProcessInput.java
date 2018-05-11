package io.skalogs.skaetl.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProcessInput {
    private String host;
    private String port;
    private String topicInput;
    private String id;

    public String bootstrapServer() {
        return host + ":" + port;
    }
}
