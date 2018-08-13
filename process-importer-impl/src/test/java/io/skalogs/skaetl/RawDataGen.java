package io.skalogs.skaetl;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class RawDataGen {
    public String type;
    public String timestamp;
    public String messageSend;
    public String project;
    public String fieldTestToRename;
    public String fieldTestToDelete;

}
