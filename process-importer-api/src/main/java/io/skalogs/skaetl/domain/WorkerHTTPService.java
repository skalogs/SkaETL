package io.skalogs.skaetl.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class WorkerHTTPService {
    @Builder.Default
    private ExternalHTTPData externalHTTPService;
    private LocalDateTime lastRefresh;
    @Builder.Default
    private HashMap<String, String> mapResult = new HashMap<>();
}
