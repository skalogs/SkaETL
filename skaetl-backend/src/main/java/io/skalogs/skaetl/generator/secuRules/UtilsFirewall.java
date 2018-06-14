package io.skalogs.skaetl.generator.secuRules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class UtilsFirewall {

    private final UtilsSecu utilsSecu;
    private Random RANDOM = new Random();
    private List<FWEquiGen> listEqui = new ArrayList<>();
    private Integer[] tabPort = new Integer[]{
            80,
            8080,
            443,
            8090,
            22,
            23
    };
    private String[] tabHost = new String[]{
            "www.google.fr",
            "www.nbc.com",
            "weather.com",
            "vs-business-contract-prod-application.intranet.infra",
            "vs-business-contract-prod-application.intranet.infra",
            "vs-business-contract-prod-application.intranet.infra",
            "vs-business-product-prod-application.intranet.infra",
            "vs-business-product-prod-application.intranet.infra",
            "vs-business-product-prod-application.intranet.infra",
            "vs-business-support-prod-application.intranet.infra",
            "vs-business-support-prod-application.intranet.infra",
            "alerting-application.intranet.com",
            "platform.client.support.intranet.com",
            "platform.client.support.intranet.com",
            "platform.client.support.intranet.com",
            "platform.client.support.intranet.com",
            "platform.client.support.intranet.com",
            "mysupport.intranet.com",
            "mypasswport.intranet.com",
            "mypasswport.intranet.com"
    };

    public UtilsFirewall(UtilsSecu utilsSecu) {
        this.utilsSecu = utilsSecu;
        listEqui = createEqui();
    }

    public void generateData(int minute) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = utilsSecu.addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
        ClientData client = utilsSecu.getClient();
        Integer port = tabPort[RANDOM.nextInt(tabPort.length)];
        String status = "OK";
        String typeConnexion = "HTTP";
        if (port.equals(443)) {
            typeConnexion = "HTTPS";
        }
        if (port.equals(22) || port.equals(23)) {
            status = "KO";
            typeConnexion = "UNKNOWN";
        }
        FWEquiGen fwd = listEqui.get(RANDOM.nextInt(listEqui.size()));
        utilsSecu.sendToKafka("firewall", Firewall.builder()
                .srcIp(client.ipClient)
                .srcPort(RANDOM.nextInt(55000) + 2048)
                .destIp(tabHost[RANDOM.nextInt(tabHost.length)])
                .destPort(port)
                .typeConnexion(typeConnexion)
                .user(client.username)
                .status(status)
                .equipment(fwd.equipment)
                .equipmentIp(fwd.equipmentIp)
                .equipmentVersion(fwd.equipmentVersion)
                .build());
    }

    List<FWEquiGen> createEqui() {
        List<FWEquiGen> fwdEquiGen = new ArrayList<>();
        fwdEquiGen.add(FWEquiGen.builder()
                .equipment("F5 NETWORK")
                .equipmentIp("10.242.18.1")
                .equipmentVersion("11.1.23.2")
                .build());
        fwdEquiGen.add(FWEquiGen.builder()
                .equipment("STORMSHIELD")
                .equipmentIp("10.242.18.2")
                .equipmentVersion("SN-300")
                .build());
        fwdEquiGen.add(FWEquiGen.builder()
                .equipment("A10 NETWORK")
                .equipmentIp("10.242.18.3")
                .equipmentVersion("A10")
                .build());
        return fwdEquiGen;
    }
}
