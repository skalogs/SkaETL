package io.skalogs.skaetl.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "snmp")
public class SnmpConfiguration {
    private String community;
    private String trapOid;
    private String ipAddress;
    private int port;
}
