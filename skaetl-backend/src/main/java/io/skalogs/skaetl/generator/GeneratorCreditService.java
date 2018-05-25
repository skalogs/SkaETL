package io.skalogs.skaetl.generator;

import io.skalogs.skaetl.generator.credit.ClientData;
import io.skalogs.skaetl.generator.credit.InputDataCredit;
import io.skalogs.skaetl.generator.credit.UtilsCreditData;
import io.skalogs.skaetl.generator.credit.UtilsProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
@Slf4j
public class GeneratorCreditService {

    private final UtilsCreditData utilsCreditData;
    private final UtilsProcess utilsProcess;

    public GeneratorCreditService(UtilsProcess utilsProcess, UtilsCreditData utilsCreditData) {
        this.utilsCreditData = utilsCreditData;
        this.utilsProcess = utilsProcess;
    }

    public void generateData(Integer timeToGenerateInMinute, Integer nbView, Integer nbCredit, Integer nbRandomRq) {
        utilsProcess.createAllProcess();
        try {
            for(int i = 0 ; i< timeToGenerateInMinute ;i++) {
                generateDataForEndToEndView(i, nbView);
                generateDataForEndToEndCreateCredit(i, nbCredit);
                generateRandonRq(i,nbRandomRq,generateScenarioMicroServiceProvider(utilsCreditData.getProvider(), UUID.randomUUID().toString()));
                generateRandonRq(i,nbRandomRq,generateScenarioMicroServiceProduct(utilsCreditData.getProduct(), UUID.randomUUID().toString()));
                generateRandonRq(i,nbRandomRq,generateScenarioMicroServiceCustomer(utilsCreditData.getClient(), UUID.randomUUID().toString()));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.error("InterruptedException ", e);
        }
    }

    private void generateRandonRq(int minute, Integer nbRandomRq, InputDataCredit inputDataCredit){
        int nbCreateRq = utilsCreditData.random(nbRandomRq);
        for(int i = 0 ; i<nbCreateRq ; i++) {
            utilsCreditData.generateScriptGlobalBackendRequest(minute, inputDataCredit);
        }
    }

    private void generateDataForEndToEndView(int minute, Integer nbView){
        for(int i = 0 ; i< nbView ;i++) {
            ClientData clientData = utilsCreditData.getClient();
            String product = utilsCreditData.getProduct();
            String provider = utilsCreditData.getProvider();
            String requestId = UUID.randomUUID().toString();
            Integer timeTotalRequest = 0;
            timeTotalRequest += utilsCreditData.generateScriptGlobalBackendRequest(minute, generateScenarioMicroServiceProvider(provider, requestId));
            timeTotalRequest += utilsCreditData.generateScriptGlobalBackendRequest(minute, generateScenarioMicroServiceProduct(product, requestId));
            timeTotalRequest += utilsCreditData.generateScriptGlobalBackendRequest(minute, generateScenarioMicroServiceCustomer(clientData, requestId));
            Integer timeFront = timeTotalRequest + utilsCreditData.random(20);
            utilsCreditData.generateScriptGlobalFrontEndRequest(minute, "front-view", "/view/espaceClient", "GET", "200", null, null, product, clientData, requestId, timeFront);
        }
    }

    private void generateDataForEndToEndCreateCredit(int minute, Integer nbCredit){
        for(int i = 0 ; i< nbCredit ;i++) {
            Integer amount = 500 + utilsCreditData.random(19) * 1000;
            Integer creditDuration = utilsCreditData.getDuration();
            String product = utilsCreditData.getProduct();
            ClientData clientData = utilsCreditData.getClient();
            String requestId = UUID.randomUUID().toString();
            Integer timeTotalRequest = 0;
            timeTotalRequest += utilsCreditData.generateScriptGlobalBackendRequest(minute, generateScenarioMicroServiceCreateCreditValidationClient(amount, creditDuration, product, clientData, requestId));
            timeTotalRequest += utilsCreditData.generateScriptGlobalBackendRequest(minute, generateScenarioMicroServiceCreateCreditValidationProduct(product, requestId));
            timeTotalRequest += utilsCreditData.generateScriptGlobalBackendRequest(minute, generateScenarioMicroServiceCreateCredit(amount, creditDuration, product, clientData, requestId));
            Integer timeFront = timeTotalRequest + utilsCreditData.random(20);
            utilsCreditData.generateScriptGlobalFrontEndRequest(minute, "front-create-credit", "/view/demandeCredit", "POST", "200", amount, creditDuration, product, clientData, requestId, timeFront);
        }
    }

    private InputDataCredit generateScenarioMicroServiceCreateCreditValidationClient(Integer amount, Integer creditDuration,String product, ClientData clientData, String requestId){
        String codeResponse = "200";
        if(utilsCreditData.random(30) == 1){
            product = "unknown";
            codeResponse = "404";
        }
        Integer timeWS= utilsCreditData.random(50);
        Integer timeBL = utilsCreditData.random(10)+timeWS;
        Integer timeGlobal = utilsCreditData.random(2)+timeBL;
        return InputDataCredit.builder()
                .apiName("create-credit")
                .uri("/credit/create")
                .requestId(requestId)
                .typeRequest("POST")
                .codeResponse(codeResponse)
                .serviceBL("validationCustomerService")
                .productName(product)
                .firstName(clientData.getFirstName())
                .lastName(clientData.getLastName())
                .email(clientData.getEmail())
                .amount(amount)
                .creditDuration(creditDuration)
                .timeGlobal(timeGlobal)
                .timeBL(timeBL)
                .nameWS("service validation client")
                .typeRequestWS("POST")
                .uriWS("/security/validationCustomer")
                .codeResponseWS(codeResponse)
                .type("credit")
                .topic("credit")
                .build();
    }


    private InputDataCredit generateScenarioMicroServiceCreateCreditValidationProduct(String product, String requestId){
        String codeResponse = "200";
        if(utilsCreditData.random(30) == 1){
            product = "unknown";
            codeResponse = "404";
        }
        Integer timeWS= utilsCreditData.random(50);
        Integer timeBL = utilsCreditData.random(10)+timeWS;
        Integer timeGlobal = utilsCreditData.random(2)+timeBL;
        return InputDataCredit.builder()
                .apiName("create-credit")
                .uri("/credit/create")
                .requestId(requestId)
                .typeRequest("POST")
                .codeResponse(codeResponse)
                .serviceBL("validationProductService")
                .productName(product)
                .timeGlobal(timeGlobal)
                .timeBL(timeBL)
                .nameWS("service validation product")
                .typeRequestWS("POST")
                .uriWS("/getProduct")
                .codeResponseWS(codeResponse)
                .type("credit")
                .topic("credit")
                .build();
    }


    private InputDataCredit generateScenarioMicroServiceCreateCredit(Integer amount, Integer creditDuration,String product, ClientData clientData, String requestId){
        String codeResponse = "200";
        if(utilsCreditData.random(30) == 1){
            product = "unknown";
            codeResponse = "404";
        }
        Integer timeDB= utilsCreditData.random(500);
        Integer timeWS= utilsCreditData.random(50)+timeDB;
        Integer timeBL = utilsCreditData.random(10)+timeWS;
        Integer timeGlobal = utilsCreditData.random(2)+timeBL;
        return InputDataCredit.builder()
                .apiName("create-credit")
                .uri("/credit/create")
                .requestId(requestId)
                .typeRequest("POST")
                .codeResponse(codeResponse)
                .provider(utilsCreditData.getProvider())
                .serviceBL("creditService")
                .database("CREDIT_PROD")
                .typeDB("INSERT")
                .requestDB("INSERT INTO (....)")
                .productName(product)
                .timeGlobal(timeGlobal)
                .timeDB(timeDB)
                .timeBL(timeBL)
                .nameWS("service validation client")
                .typeRequestWS("POST")
                .uriWS("/security/validationCustomer")
                .codeResponseWS(codeResponse)
                .amount(amount)
                .creditDuration(creditDuration)
                .firstName(clientData.getFirstName())
                .lastName(clientData.getLastName())
                .email(clientData.getEmail())
                .type("credit")
                .topic("credit")
                .build();
    }

    private InputDataCredit generateScenarioMicroServiceCustomer(ClientData clientData, String requestId){
        String codeResponse = "200";
        if(utilsCreditData.random(10) == 1){
            codeResponse = "404";
        }
        Integer timeDB= utilsCreditData.random(10);
        Integer timeWS= utilsCreditData.random(3000)+timeDB;
        Integer timeBL = utilsCreditData.random(10)+timeWS;
        Integer timeGlobal = utilsCreditData.random(2)+timeBL;
        return InputDataCredit.builder()
                .apiName("find-customer")
                .uri("/getCustomer")
                .requestId(requestId)
                .typeRequest("GET")
                .codeResponse(codeResponse)
                .serviceBL("customerService")
                .database("CUSTOMER_PROD")
                .typeDB("SELECT")
                .requestDB("SELECT * FROM CUSTOMER WHERE firstname='"+clientData.getFirstName()+"' and lastname='"+clientData.getLastName()+"' ans email='"+clientData.getEmail()+"'")
                .firstName(clientData.getFirstName())
                .lastName(clientData.getLastName())
                .email(clientData.getEmail())
                .timeGlobal(timeGlobal)
                .timeDB(timeDB)
                .timeBL(timeBL)
                .timeWS(timeWS)
                .nameWS("service validation client")
                .typeRequestWS("POST")
                .uriWS("/security/validationCustomer")
                .codeResponseWS(codeResponse)
                .type("customer")
                .topic("customer")
                .build();
    }

    private InputDataCredit generateScenarioMicroServiceProduct(String product, String requestId){
        String codeResponse = "200";
        if(utilsCreditData.random(50) == 1){
            product = "unknown";
            codeResponse = "404";
        }
        Integer timeDB= utilsCreditData.random(40);
        Integer timeBL = utilsCreditData.random(10)+timeDB;
        Integer timeGlobal = utilsCreditData.random(2)+timeBL;
        return InputDataCredit.builder()
                .apiName("find-product")
                .uri("/getProduct")
                .requestId(requestId)
                .typeRequest("GET")
                .codeResponse(codeResponse)
                .serviceBL("productService")
                .database("PRODUCT_PROD")
                .typeDB("SELECT")
                .requestDB("SELECT * FROM PRODUCT_REFERENCE WHERE product_name='"+product+"'")
                .productName(product)
                .timeGlobal(timeGlobal)
                .timeDB(timeDB)
                .timeBL(timeBL)
                .type("product")
                .topic("product")
                .build();
    }

    private InputDataCredit generateScenarioMicroServiceProvider(String provider, String requestId){
        String codeResponse = "200";
        if(utilsCreditData.random(50) == 1){
            provider = "unknown";
            codeResponse = "404";
        }
        Integer timeDB= utilsCreditData.random(20);
        Integer timeBL = utilsCreditData.random(2)+timeDB;
        Integer timeGlobal = utilsCreditData.random(2)+timeBL;
        return InputDataCredit.builder()
                .apiName("find-provider")
                .uri("/listProvider")
                .requestId(requestId)
                .typeRequest("GET")
                .codeResponse(codeResponse)
                .serviceBL("providerListReferentialService")
                .database("PROVIDER_PROD")
                .typeDB("SELECT")
                .requestDB("SELECT * FROM PROVIDER_REFERENCE WHERE provider_name='"+provider+"'")
                .provider(provider)
                .timeGlobal(timeGlobal)
                .timeDB(timeDB)
                .timeBL(timeBL)
                .type("provider")
                .topic("provider")
                .build();
    }
}
