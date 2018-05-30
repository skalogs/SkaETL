package io.skalogs.skaetl.generator.credit;

import com.google.common.collect.Lists;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.service.ProcessService;
import io.skalogs.skaetl.service.ReferentialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class UtilsCreditProcess {

    private final ProcessService processService;
    private final ReferentialService referentialService;
    private final String host;
    private final String port;

    private final String idProcessCreditData = "idProcessCreditData";
    private final String idProcessProviderData = "idProcessProviderData";
    private final String idProcessProductData = "idProcessProductData";
    private final String idProcessCustomerData = "idProcessCustomerData";
    private final String idProcessFrontData = "idProcessFrontData";

    private final HashMap<String,String> mapProduct;

    public UtilsCreditProcess(ProcessService processService, UtilsCreditData utilsCreditData, KafkaConfiguration kafkaConfiguration, ReferentialService referentialService) {
        this.processService = processService;
        this.referentialService = referentialService;
        this.host = kafkaConfiguration.getBootstrapServers().split(":")[0];
        this.port = kafkaConfiguration.getBootstrapServers().split(":")[1];
        this.mapProduct = new HashMap<>();
        this.mapProduct.put("AUTOM","credit automobile");
        this.mapProduct.put("CONSO","credit consommation");
        this.mapProduct.put("TRAVA","credit travaux");
        this.mapProduct.put("ECOLO","credit renovation ecologique");
        this.mapProduct.put("REVOL","credit revolving");

    }

    private void createReferentialCredit(){
        //Track db_ip
        //validation -> if no activity on status credit 1 day -> produce a message for inactivity
        //notification -> if statusCredit change -> produce a message for change
        referentialService.updateReferential(ProcessReferential.builder()
                .name("referentialCreditStatus")
                .idProcess("demoReferentialCreditStatus")
                .referentialKey("Client")
                .listIdProcessConsumer(Lists.newArrayList(idProcessCreditData))
                .listAssociatedKeys(Lists.newArrayList("email"))
                .listMetadata(Lists.newArrayList("statusCredit","creditDuration","productName","user"))
                .isValidationTimeField(true)
                .fieldChangeValidation("statusCredit")
                .timeValidationInSec(24*60*60)
                .isNotificationChange(true)
                .fieldChangeNotification("statusCredit")
                .build());
        try {
            Thread.sleep(2000);
            referentialService.activateProcess((ProcessReferential) referentialService.findReferential("demoReferentialCreditStatus"));
        }catch (Exception e){
            log.error("Exception {}",e);
        }
    }

    public void createAllReferential(){
        createReferentialCredit();
    }

    public void createAllProcess(){
        createAndActiveProcessConsumerCredit();
        createAndActiveProcessConsumerCustomer();
        createAndActiveProcessConsumerFront();
        createAndActiveProcessConsumerProduct();
        createAndActiveProcessConsumerProvider();
    }

    private void createAndActiveProcessConsumerCustomer() {
        if (processService.findProcess(idProcessCustomerData) == null) {
            List<ProcessTransformation> listProcessTransformation = new ArrayList<>();
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.ADD_FIELD)
                    .parameterTransformation(ParameterTransformation.builder()
                            .composeField(ProcessKeyValue.builder().key("project").value("demo-credit").build())
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_LONG)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("timeRequestMs")
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_EMAIL)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("email")
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.HASH)
                    .parameterTransformation(ParameterTransformation.builder()
                            .processHashData(ProcessHashData.builder().typeHash(TypeHash.SHA256).field("firstName").build())
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.HASH)
                    .parameterTransformation(ParameterTransformation.builder()
                            .processHashData(ProcessHashData.builder().typeHash(TypeHash.SHA256).field("lastName").build())
                            .build())
                    .build());
            processService.saveOrUpdate(ProcessConsumer.builder()
                    .idProcess(idProcessCustomerData)
                    .name("demo credit customer")
                    .processInput(ProcessInput.builder().topicInput("customer").host(this.host).port(this.port).build())
                    .processTransformation(listProcessTransformation)
                    .processOutput(Lists.newArrayList(
                            ProcessOutput.builder().typeOutput(TypeOutput.ELASTICSEARCH).parameterOutput(ParameterOutput.builder().elasticsearchRetentionLevel(RetentionLevel.week).build()).build()))
                    .build());

            try {
                //HACK
                Thread.sleep(2000);
                processService.activateProcess(processService.findProcess(idProcessCustomerData));
            } catch (Exception e) {
                log.error("Exception createAndActiveProcessConsumer {}", idProcessCustomerData);
            }
        }
    }


    private void createAndActiveProcessConsumerProvider() {
        if (processService.findProcess(idProcessProviderData) == null) {
            List<ProcessTransformation> listProcessTransformation = new ArrayList<>();
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.ADD_FIELD)
                    .parameterTransformation(ParameterTransformation.builder()
                            .composeField(ProcessKeyValue.builder().key("project").value("demo-credit").build())
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_LONG)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("timeRequestMs")
                            .build())
                    .build());

            processService.saveOrUpdate(ProcessConsumer.builder()
                    .idProcess(idProcessProviderData)
                    .name("demo credit provider")
                    .processInput(ProcessInput.builder().topicInput("provider").host(this.host).port(this.port).build())
                    .processTransformation(listProcessTransformation)
                    .processOutput(Lists.newArrayList(
                            ProcessOutput.builder().typeOutput(TypeOutput.ELASTICSEARCH).parameterOutput(ParameterOutput.builder().elasticsearchRetentionLevel(RetentionLevel.week).build()).build()))
                    .build());

            try {
                //HACK
                Thread.sleep(2000);
                processService.activateProcess(processService.findProcess(idProcessProviderData));
            } catch (Exception e) {
                log.error("Exception createAndActiveProcessConsumer {}", idProcessProviderData);
            }
        }
    }

    private void createAndActiveProcessConsumerProduct() {
        if (processService.findProcess(idProcessProductData) == null) {
            List<ProcessTransformation> listProcessTransformation = new ArrayList<>();
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.ADD_FIELD)
                    .parameterTransformation(ParameterTransformation.builder()
                            .composeField(ProcessKeyValue.builder().key("project").value("demo-credit").build())
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_LONG)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("timeRequestMs")
                            .build())
                    .build());

            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.LOOKUP_LIST)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("productName")
                            .mapLookup(mapProduct)
                            .build())
                    .build());

            processService.saveOrUpdate(ProcessConsumer.builder()
                    .idProcess(idProcessProductData)
                    .name("demo credit product")
                    .processInput(ProcessInput.builder().topicInput("product").host(this.host).port(this.port).build())
                    .processTransformation(listProcessTransformation)
                    .processOutput(Lists.newArrayList(
                            ProcessOutput.builder().typeOutput(TypeOutput.ELASTICSEARCH).parameterOutput(ParameterOutput.builder().elasticsearchRetentionLevel(RetentionLevel.week).build()).build()))
                    .build());

            try {
                //HACK
                Thread.sleep(2000);
                processService.activateProcess(processService.findProcess(idProcessProductData));
            } catch (Exception e) {
                log.error("Exception createAndActiveProcessConsumer {}", idProcessProductData);
            }
        }
    }

    private void createAndActiveProcessConsumerCredit() {
        if (processService.findProcess(idProcessCreditData) == null) {
            List<ProcessTransformation> listProcessTransformation = new ArrayList<>();
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.ADD_FIELD)
                    .parameterTransformation(ParameterTransformation.builder()
                            .composeField(ProcessKeyValue.builder().key("project").value("demo-credit").build())
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_LONG)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("timeRequestMs")
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_LONG)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("amount")
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_EMAIL)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("email")
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.LOOKUP_LIST)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("productName")
                            .mapLookup(mapProduct)
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.HASH)
                    .parameterTransformation(ParameterTransformation.builder()
                            .processHashData(ProcessHashData.builder().typeHash(TypeHash.SHA256).field("firstName").build())
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.HASH)
                    .parameterTransformation(ParameterTransformation.builder()
                            .processHashData(ProcessHashData.builder().typeHash(TypeHash.SHA256).field("lastName").build())
                            .build())
                    .build());


            processService.saveOrUpdate(ProcessConsumer.builder()
                    .idProcess(idProcessCreditData)
                    .name("demo credit process")
                    .processInput(ProcessInput.builder().topicInput("credit").host(this.host).port(this.port).build())
                    .processTransformation(listProcessTransformation)
                    .processOutput(Lists.newArrayList(
                            ProcessOutput.builder().typeOutput(TypeOutput.ELASTICSEARCH).parameterOutput(ParameterOutput.builder().elasticsearchRetentionLevel(RetentionLevel.week).build()).build()))
                    .build());

            try {
                //HACK
                Thread.sleep(2000);
                processService.activateProcess(processService.findProcess(idProcessCreditData));
            } catch (Exception e) {
                log.error("Exception createAndActiveProcessConsumer {}", idProcessCreditData);
            }
        }
    }

    private void createAndActiveProcessConsumerFront() {
        if (processService.findProcess(idProcessFrontData) == null) {
            List<ProcessTransformation> listProcessTransformation = new ArrayList<>();
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.ADD_FIELD)
                    .parameterTransformation(ParameterTransformation.builder()
                            .composeField(ProcessKeyValue.builder().key("project").value("demo-credit").build())
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_LONG)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("timeRequestMs")
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_EMAIL)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("email")
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.LOOKUP_LIST)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("productName")
                            .mapLookup(mapProduct)
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.HASH)
                    .parameterTransformation(ParameterTransformation.builder()
                            .processHashData(ProcessHashData.builder().typeHash(TypeHash.SHA256).field("firstName").build())
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.HASH)
                    .parameterTransformation(ParameterTransformation.builder()
                            .processHashData(ProcessHashData.builder().typeHash(TypeHash.SHA256).field("lastName").build())
                            .build())
                    .build());
            listProcessTransformation.add(ProcessTransformation.builder()
                    .typeTransformation(TypeValidation.FORMAT_LONG)
                    .parameterTransformation(ParameterTransformation.builder()
                            .keyField("timeRequestMs")
                            .build())
                    .build());
            processService.saveOrUpdate(ProcessConsumer.builder()
                    .idProcess(idProcessFrontData)
                    .name("demo credit front")
                    .processInput(ProcessInput.builder().topicInput("front").host(this.host).port(this.port).build())
                    .processTransformation(listProcessTransformation)
                    .processOutput(Lists.newArrayList(
                            ProcessOutput.builder().typeOutput(TypeOutput.ELASTICSEARCH).parameterOutput(ParameterOutput.builder().elasticsearchRetentionLevel(RetentionLevel.week).build()).build()))
                    .build());

            try {
                //HACK
                Thread.sleep(2000);
                processService.activateProcess(processService.findProcess(idProcessFrontData));
            } catch (Exception e) {
                log.error("Exception createAndActiveProcessConsumer {}", idProcessFrontData);
            }
        }
    }

    private ProcessOutput toEsOutput() {
        return ProcessOutput.builder()
                .typeOutput(TypeOutput.ELASTICSEARCH)
                .parameterOutput(ParameterOutput.builder().elasticsearchRetentionLevel(RetentionLevel.week).build())
                .build();
    }

}
