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

import com.google.common.collect.Lists;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.generator.cart.UtilsCartData;
import io.skalogs.skaetl.service.MetricServiceHTTP;
import io.skalogs.skaetl.service.ProcessServiceHTTP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class GeneratorCartService {

    private final ProcessServiceHTTP processServiceHTTP;
    private final MetricServiceHTTP metricServiceHTTP;
    private final UtilsCartData utilsCartData;
    private final String host;
    private final String port;
    private Random RANDOM = new Random();

    public GeneratorCartService(ProcessServiceHTTP processServiceHTTP, MetricServiceHTTP metricServiceHTTP, UtilsCartData utilsCartData, KafkaConfiguration kafkaConfiguration) {
        this.processServiceHTTP = processServiceHTTP;
        this.metricServiceHTTP = metricServiceHTTP;
        this.utilsCartData =  utilsCartData;
        this.host = kafkaConfiguration.getBootstrapServers().split(":")[0];
        this.port = kafkaConfiguration.getBootstrapServers().split(":")[1];
    }


    private void createAndActiveProcessConsumer() {
        if (processServiceHTTP.findProcess("idProcessCartData") == null) {
            List<ProcessTransformation> listProcessTransformation = new ArrayList<>();
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.ADD_FIELD)
                    .parameterTransformation(ParameterTransformation.builder()
                            .composeField(ProcessKeyValue.builder().key("project").value("demo-cart").build())
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.RENAME_FIELD)
                    .parameterTransformation(ParameterTransformation.builder()
                            .composeField(ProcessKeyValue.builder().key("name").value("product-name").build())
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_DOUBLE)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("totalItemPrice")
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_LONG)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("discount")
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_LONG)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("quantity")
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_EMAIL)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("customerEmail")
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.ADD_GEO_LOCALISATION)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("ip")
                            .build())
                    .build());

            processServiceHTTP.saveOrUpdate(ProcessConsumer.builder()
                    .idProcess("idProcessCartData")
                    .name("demo cart")
                    .processInput(ProcessInput.builder().topicInput("demo-cart").host(this.host).port(this.port).build())
                    .processTransformation(listProcessTransformation)
                    .processOutput(Lists.newArrayList(
                            ProcessOutput.builder().typeOutput(TypeOutput.ELASTICSEARCH).parameterOutput(ParameterOutput.builder().elasticsearchRetentionLevel(RetentionLevel.week).build()).build()))
                    .build());

            try {
                //HACK
                Thread.sleep(2000);
                processServiceHTTP.activateProcess(processServiceHTTP.findProcess("idProcessCartData"));
            } catch (Exception e) {
                log.error("Exception createAndActiveProcessConsumer idProcessCartData");
            }

            buildMetrics();
        }
    }

    private void buildMetrics() {
        List<ProcessMetric> processMetrics = new ArrayList<>();
        //TX
        processMetrics.add(ProcessMetric.builder()
                .idProcess("CART_NB_TX")
                .name("Cart - Number of Transaction")
                .sourceProcessConsumers(Lists.newArrayList("idProcessCartData"))
                .aggFunction("COUNT(*)")
                .where("type = \"payment\"")
                .windowType(WindowType.TUMBLING)
                .size(2)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        processMetrics.add(ProcessMetric.builder()
                .idProcess("CART_NB_TX_FAIL")
                .name("Cart - Number of Transaction Failed")
                .sourceProcessConsumers(Lists.newArrayList("idProcessCartData"))
                .aggFunction("COUNT(*)")
                .where("type = \"incident\"")
                .windowType(WindowType.TUMBLING)
                .size(2)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        //PRODUCT
        processMetrics.add(ProcessMetric.builder()
                .idProcess("CART_NB_PRODUCT_SEEN")
                .name("Cart - Number of Product seen")
                .sourceProcessConsumers(Lists.newArrayList("idProcessCartData"))
                .aggFunction("COUNT(*)")
                .where("type = \"showProduct\"")
                .windowType(WindowType.TUMBLING)
                .size(2)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        processMetrics.add(ProcessMetric.builder()
                .idProcess("CART_NB_PRODUCT_SEEN_PER_USER")
                .name("Cart - Number of Product seen per user")
                .sourceProcessConsumers(Lists.newArrayList("idProcessCartData"))
                .aggFunction("COUNT(*)")
                .where("type = \"showProduct\"")
                .groupBy("customerEmail_ue")
                .windowType(WindowType.TUMBLING)
                .size(2)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        processMetrics.add(ProcessMetric.builder()
                .idProcess("CART_NB_PRODUCT_SOLD")
                .name("Cart - Number of Product Sold")
                .sourceProcessConsumers(Lists.newArrayList("idProcessCartData"))
                .aggFunction("COUNT(*)")
                .where("type = \"addToCart\"")
                .groupBy("product-name")
                .windowType(WindowType.TUMBLING)
                .size(2)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        //DISCOUNT
        processMetrics.add(ProcessMetric.builder()
                .idProcess("CART_NB_OF_DISCOUNT_PER_USER")
                .name("Cart - Number of Discount per User")
                .sourceProcessConsumers(Lists.newArrayList("idProcessCartData"))
                .aggFunction("COUNT(*)")
                .where("type = \"payment\"")
                .groupBy("customerEmail_ue")
                .windowType(WindowType.TUMBLING)
                .size(2)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        processMetrics.add(ProcessMetric.builder()
                .idProcess("CART_AVG_DISCOUNT_PER_USER")
                .name("Cart - Average Discount per User")
                .sourceProcessConsumers(Lists.newArrayList("idProcessCartData"))
                .aggFunction("AVG(discount_long)")
                .where("type = \"payment\"")
                .groupBy("customerEmail_ue")
                .windowType(WindowType.TUMBLING)
                .size(2)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        //FRAUD
        processMetrics.add(ProcessMetric.builder()
                .idProcess("CART_FRAUD_DIFFERENT_COUNTRY")
                .name("Cart - Fraud different country")
                .sourceProcessConsumers(Lists.newArrayList("idProcessCartData"))
                .aggFunction("COUNT-DISTINCT(ip_country_name)")
                .where("type IN(\"payment\",\"incident\") ")
                .groupBy("customerEmail_ue")
                .having("> 1")
                .windowType(WindowType.TUMBLING)
                .size(10)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        processMetrics.add(ProcessMetric.builder()
                .idProcess("CART_FRAUD_DISCOUNT_USER")
                .name("Cart - Fraud discount same user")
                .sourceProcessConsumers(Lists.newArrayList("idProcessCartData"))
                .aggFunction("COUNT-DISTINCT(codeDiscount)")
                .where("type = \"payment\" ")
                .groupBy("customerEmail_ue")
                .having("> 1")
                .windowType(WindowType.TUMBLING)
                .size(10)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        processMetrics.add(ProcessMetric.builder()
                .idProcess("CART_USAGE_CODE_DISCOUNT")
                .name("Cart - Usage code discount DISCOUNT-201805")
                .sourceProcessConsumers(Lists.newArrayList("idProcessCartData"))
                .aggFunction("COUNT(codeDiscount)")
                .where("type = \"payment\" AND codeDiscount =\"DISCOUNT-201805\"")
                .having("> 1")
                .windowType(WindowType.TUMBLING)
                .size(10)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        createAndActivateMetrics(processMetrics);
    }

    private ProcessOutput toEsOutput() {
        return ProcessOutput.builder()
                .typeOutput(TypeOutput.ELASTICSEARCH)
                .parameterOutput(ParameterOutput.builder().elasticsearchRetentionLevel(RetentionLevel.week).build())
                .build();
    }

    private void createAndActivateMetrics(List<ProcessMetric> processMetrics) {
        for (ProcessMetric processMetric : processMetrics) {
            metricServiceHTTP.updateProcess(processMetric);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //don't care;
        }
        for (ProcessMetric processMetric : processMetrics) {
            try {
                metricServiceHTTP.activateProcess(processMetric);
            } catch (Exception e) {
                log.error("Error occured when activating metrics " + processMetric.getIdProcess(), e);
            }
        }
    }


    public void generateData(Integer nbCustomer, Integer nbShowByMinute, Integer nbAddToCartByMinute, Integer nbPaySuccessByMinute,Integer nbPayNotSuccessByMinute, Integer timeToGenerateInMinute){
        createAndActiveProcessConsumer();
        List<String> listCustomer = utilsCartData.generateCustomer(nbCustomer);
        for(int i = 0 ; i< timeToGenerateInMinute ;i++){
            try {
                    utilsCartData.generateScriptShowProduct(nbShowByMinute,i,listCustomer);
                    utilsCartData.generateScriptAddToCart(nbAddToCartByMinute,i,utilsCartData.getUser(listCustomer),utilsCartData.generateIp(),RANDOM.nextInt(3)+1);
                    utilsCartData.generateScriptPaySucess(nbPaySuccessByMinute, i,utilsCartData.getUser(listCustomer),utilsCartData.generateIp(),RANDOM.nextInt(3)+1);
                    utilsCartData.generateScriptPayNotSucess(nbPayNotSuccessByMinute, i,utilsCartData.getUser(listCustomer),utilsCartData.generateIp(),RANDOM.nextInt(3)+1);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("InterruptedException ",e);
            }
        }
    }

    public void generateSpecificUsecase(Integer nbCustomer){
        List<String> listCustomer = utilsCartData.generateCustomer(nbCustomer);
        //Specific usecase
        //Same user different ip, Buy Or Incident
        utilsCartData.generateScriptPaySameCustomerDifferentIp(0,utilsCartData.getUser(listCustomer));
        //Same user different codeDiscount
        utilsCartData.generateScriptPaySameCustomerDifferentDiscount(0,utilsCartData.getUser(listCustomer));
    }

}
