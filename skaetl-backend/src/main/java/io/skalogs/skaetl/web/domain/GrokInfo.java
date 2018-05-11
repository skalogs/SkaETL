package io.skalogs.skaetl.web.domain;

import lombok.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GrokInfo {
    private String patternToCompile;
    private String data;
    private List<String> listData;
    private String topic;
    private long duration;
    private TimeUnit timeUnit;
}
