package io.skalogs.skaetl.generator.secuRules;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Firewall {
    public Long id;
    // Timestamp Request
    public String timestamp;
    // Login or uuid
    public String user;

    // Source
    public String srcIp;
    // port src
    @JsonProperty("src_port_l")
    public Integer srcPort;
    // Dest
    public String destIp;
    // port Dest
    @JsonProperty("dest_port_l")
    public Integer destPort;

    // Status Blocked, Warning, Accept
    public String status;
    public String typeConnexion;
    // Name of equipment
    public String equipment;
    // Version of equipment
    public String equipmentVersion;
    // Ip of equipment
    public String equipmentIp;


}
