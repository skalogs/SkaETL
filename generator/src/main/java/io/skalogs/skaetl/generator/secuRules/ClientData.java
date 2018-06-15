package io.skalogs.skaetl.generator.secuRules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientData {
    public String ipClient;
    public String hostname;
    public String username;
}
