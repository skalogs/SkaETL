package io.skalogs.skaetl.generator;

import com.google.common.collect.Lists;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.generator.cart.UtilsCartData;
import io.skalogs.skaetl.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@Slf4j
public class GeneratorCartService {

    private final ProcessService processService;
    private final UtilsCartData utilsCartData;
    private Random RANDOM = new Random();

    public GeneratorCartService(ProcessService processService, UtilsCartData utilsCartData) {
        this.processService = processService;
        this.utilsCartData =  utilsCartData;
    }


    private void createAndActiveProcessConsumer() {
        processService.saveOrUpdate(ProcessConsumer.builder()
                .idProcess("idProcessCardData")
                .processInput(ProcessInput.builder().topicInput("demo-cart").host("kafka.kafka").port("9092").build())
                .processOutput(Lists.newArrayList(
                        ProcessOutput.builder().typeOutput(TypeOutput.ELASTICSEARCH).parameterOutput(ParameterOutput.builder().elasticsearchRetentionLevel(RetentionLevel.week).build()).build()))
                .build());
        try {
            processService.activateProcess(processService.findProcess("idProcessCardData"));
        } catch (Exception e) {
            log.error("Exception createAndActiveProcessConsumer idProcessCardData");
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
