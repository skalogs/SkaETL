package io.skalogs.skaetl.generator.secuRules;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConnexionSSH {
    public String timestamp;
    public String serverIp;
    public String messageRaw;
    public String clientIp;
    @JsonProperty("client_port_l")
    public Integer portClient;
    public String userClient;
    public String status;
    public String hostname;
    public String osHost;
    public String osVersion;
    public String osPatch;
}
