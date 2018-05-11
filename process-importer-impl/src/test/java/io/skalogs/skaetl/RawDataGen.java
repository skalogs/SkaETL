package io.skalogs.skaetl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class RawDataGen {
    public String type;
    @JsonProperty("@timestamp")
    public String timestamp;
    public String messageSend;
    public String project;
    public String fieldTestToRename;
    public String fieldTestToDelete;

}
