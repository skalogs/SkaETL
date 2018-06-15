package io.skalogs.skaetl.generator.secuRules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
@Slf4j
public class UtilsConnexion {

    private final UtilsSecu utilsSecu;
    private Random RANDOM = new Random();
    private String[] tabOsServer = new String[]{
            "RHEL"
    };
    private String[] tabVersion = new String[]{
            "7.3",
            "7.1",
            "6.3",
            "7.3",
            "7.3",
            "7.3",
            "7.3",
            "7.3",
            "7.3",
            "7.3"
    };
    private String[] tabPatch = new String[]{
            ""
    };

    public UtilsConnexion(UtilsSecu utilsSecu) {
        this.utilsSecu = utilsSecu;
    }

    public void generateData(int minute) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = utilsSecu.addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
        ClientData client = utilsSecu.getClient();
        String status = "OK";
        int rand = RANDOM.nextInt(10);
        if (rand == 5) {
            status = "KO";
        }
        utilsSecu.sendToKafka("connexion", ConnexionSSH.builder()
                .clientIp(client.ipClient)
                .portClient(new Integer(22))
                .serverIp("10.15.5." + +RANDOM.nextInt(220))
                .hostname(client.hostname)
                .messageRaw("log ssh")
                .osHost(tabOsServer[RANDOM.nextInt(tabOsServer.length)])
                .osPatch(tabPatch[RANDOM.nextInt(tabPatch.length)])
                .osVersion(tabVersion[RANDOM.nextInt(tabVersion.length)])
                .timestamp(df.format(newDate))
                .userClient(client.username)
                .status(status)
                .build());
    }
}
