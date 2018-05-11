package io.skalogs.skaetl.domain;


import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RegistryWorker {
    private String ip;
    private String port;
    private String name;
    private StatusWorker status;
    private Date dateRefresh;
    private WorkerType workerType;
    @Builder.Default
    private List<StatusConsumer> statusConsumerList = new ArrayList<>();

    public String getBaseUrl() {
        return "http://" + ip + ":" + port;
    }

    public boolean hasRunning(String id) {
        return statusConsumerList
                .stream()
                .filter(consumer -> consumer.getStatusProcess() == StatusProcess.ENABLE)
                .anyMatch(consumer -> consumer.getIdProcessConsumer().equals(id));
    }

    public String getFQDN() {
        return name + "-" + workerType.name();
    }
}
