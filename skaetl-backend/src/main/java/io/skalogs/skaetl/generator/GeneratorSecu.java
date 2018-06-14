package io.skalogs.skaetl.generator;

import io.skalogs.skaetl.generator.secuRules.UtilsConnexion;
import io.skalogs.skaetl.generator.secuRules.UtilsFirewall;
import io.skalogs.skaetl.generator.secuRules.UtilsSecu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GeneratorSecu {

    private final UtilsSecu utilsSecu;
    private final UtilsConnexion utilsConnexion;
    private final UtilsFirewall utilsFirewall;

    public GeneratorSecu(UtilsSecu utilsSecu, UtilsConnexion utilsConnexion, UtilsFirewall utilsFirewall) {
        this.utilsSecu = utilsSecu;
        this.utilsConnexion = utilsConnexion;
        this.utilsFirewall = utilsFirewall;
    }

    public void generateLongData(Integer timeToGenerateInHours, Boolean firewall, Boolean proxy, Boolean connexion, Boolean database, Integer nbUser) {
        try {
            utilsSecu.setup(nbUser);
            Thread.sleep(5000);
            Integer timeToGenerateInMinute = timeToGenerateInHours * 60;
            for (int i = 0; i < timeToGenerateInMinute; i++) {
                if (connexion) {
                    utilsConnexion.generateData(i);
                }
                if (firewall) {
                    utilsFirewall.generateData(i);
                }

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.error("InterruptedException ", e);
        }
    }

}
