package io.skalogs.skaetl.generator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class RawApacheDataGen {
    public String type;
    public String timestamp;
    public String request;
    public String project;
    @JsonProperty("client_ip")
    public String clientip;
    public String auth;
    public String response;
    public String verb;
    public String bytes;
    @JsonProperty("http_version")
    public String httpversion;
}