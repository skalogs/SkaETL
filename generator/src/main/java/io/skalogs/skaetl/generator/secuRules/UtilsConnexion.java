package io.skalogs.skaetl.generator.secuRules;

/*-
 * #%L
 * generator
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
