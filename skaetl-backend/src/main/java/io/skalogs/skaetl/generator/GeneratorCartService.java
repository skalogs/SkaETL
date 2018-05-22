package io.skalogs.skaetl.generator;

import com.google.common.collect.Lists;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.generator.cart.UtilsCartData;
import io.skalogs.skaetl.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class GeneratorCartService {

    private final ProcessService processService;
    private final UtilsCartData utilsCartData;
    private final String host;
    private final String port;
    private Random RANDOM = new Random();

    public GeneratorCartService(ProcessService processService, UtilsCartData utilsCartData, KafkaConfiguration kafkaConfiguration) {
        this.processService = processService;
        this.utilsCartData =  utilsCartData;
        this.host = kafkaConfiguration.getBootstrapServers().split(":")[0];
        this.port = kafkaConfiguration.getBootstrapServers().split(":")[1];
    }


    private void createAndActiveProcessConsumer() {
        if(processService.findProcess("idProcessCardData") == null) {
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
            //IP

            processService.saveOrUpdate(ProcessConsumer.builder()
                    .idProcess("idProcessCardData")
                    .name("demo cart")
                    .processInput(ProcessInput.builder().topicInput("demo-cart").host(this.host).port(this.port).build())
                    .processTransformation(listProcessTransformation)
                    .processOutput(Lists.newArrayList(
                            ProcessOutput.builder().typeOutput(TypeOutput.ELASTICSEARCH).parameterOutput(ParameterOutput.builder().elasticsearchRetentionLevel(RetentionLevel.week).build()).build()))
                    .build());
            try {
                processService.activateProcess(processService.findProcess("idProcessCardData"));
            } catch (Exception e) {
                log.error("Exception createAndActiveProcessConsumer idProcessCardData");
            }
        }
    }

    public void generateData(Integer nbCustomer, Integer nbShowByMinute, Integer nbAddToCardByMinute, Integer nbPaySuccessByMinute,Integer nbPayNotSuccessByMinute, Integer timeToGenerateInMinute){
        createAndActiveProcessConsumer();
        List<String> listCustomer = utilsCartData.generateCustomer(nbCustomer);
        for(int i = 0 ; i< timeToGenerateInMinute ;i++){
            try {
                    utilsCartData.generateScriptShowProduct(nbShowByMinute,i,listCustomer);
                    utilsCartData.generateScriptAddToCart(nbAddToCardByMinute,i,utilsCartData.getUser(listCustomer),RANDOM.nextInt(3)+1);
                    utilsCartData.generateScriptPaySucess(nbPaySuccessByMinute, i,utilsCartData.getUser(listCustomer),RANDOM.nextInt(3)+1);
                    utilsCartData.generateScriptPayNotSucess(nbPayNotSuccessByMinute, i,utilsCartData.getUser(listCustomer),RANDOM.nextInt(3)+1);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("InterruptedException ",e);
            }
        }
    }

}
