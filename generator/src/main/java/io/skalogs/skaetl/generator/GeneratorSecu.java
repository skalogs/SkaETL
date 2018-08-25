package io.skalogs.skaetl.generator;

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
