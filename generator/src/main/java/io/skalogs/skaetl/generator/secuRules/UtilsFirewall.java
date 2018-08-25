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

import lombok.Getter;
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
    @Getter
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
        int rand = RANDOM.nextInt(10);
        if (rand == 5) {
            status = "KO";
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
                .timestamp(df.format(newDate))
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
