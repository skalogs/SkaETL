package io.skalogs.skaetl.generator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class RawNetworkDataGen {
    public String type;
    @JsonProperty("timestamp")
    public String timestamp;
    public String messageSend;
    public String project;
    @JsonProperty("src_ip")
    public String srcIp;
    @JsonProperty("dest_ip")
    public String destIp;
    @JsonProperty("database_ip")
    public String databaseIp;
    //metadata
    @JsonProperty("os_server")
    public String osServer;
    @JsonProperty("database_type")
    public String typeDatabase;


}
