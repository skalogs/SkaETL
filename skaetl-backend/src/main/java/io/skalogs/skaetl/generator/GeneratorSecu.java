package io.skalogs.skaetl.generator;

import io.skalogs.skaetl.generator.secuRules.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GeneratorSecu {

    private final UtilsSecu utilsSecu;
    private final UtilsConnexion utilsConnexion;
    private final UtilsFirewall utilsFirewall;
    private final UtilsDatabase utilsDatabase;
    private final UtilsProxy utilsProxy;

    public GeneratorSecu(UtilsSecu utilsSecu, UtilsConnexion utilsConnexion, UtilsFirewall utilsFirewall, UtilsProxy utilsProxy, UtilsDatabase utilsDatabase) {
        this.utilsSecu = utilsSecu;
        this.utilsConnexion = utilsConnexion;
        this.utilsFirewall = utilsFirewall;
        this.utilsProxy = utilsProxy;
        this.utilsDatabase = utilsDatabase;
    }

    public void generateLongData(Integer timeToGenerateInMinute, Boolean firewall, Boolean proxy, Boolean connexion, Boolean database, Integer nbUser) {
        try {
            utilsSecu.setup(nbUser);
            Thread.sleep(5000);
            for (int i = 0; i < timeToGenerateInMinute; i++) {
                if (connexion) {
                    utilsConnexion.generateData(i);
                    utilsConnexion.generateData(i);
                }
                if (firewall) {
                    utilsFirewall.generateData(i);
                    utilsFirewall.generateData(i);
                    utilsFirewall.generateData(i);
                    utilsFirewall.generateData(i);
                    utilsFirewall.generateData(i);
                }
                if (database){
                    utilsDatabase.generateData(i);
                }
                if (proxy){
                    utilsProxy.generateData(i);
                    utilsProxy.generateData(i);
                    utilsProxy.generateData(i);
                    utilsProxy.generateData(i);
                    utilsProxy.generateData(i);
                }
                utilsSecu.usecase(i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.error("InterruptedException ", e);
        }
    }




}
