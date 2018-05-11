package io.skalogs.skaetl.domain;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class ClientLogstash {
    private String hostname;
    private Date dateActivity;
    private String env;
}
