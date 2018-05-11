package io.skalogs.skaetl.generator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class RawDataGen {
    public String type;
    @JsonProperty("@timestamp")
    public String timestamp;
    public String messageSend;
    public String project;
    public String fieldTestToRename;
    public String fieldTestToDelete;

}
