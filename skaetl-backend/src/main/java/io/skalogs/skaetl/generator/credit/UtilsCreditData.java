package io.skalogs.skaetl.generator.credit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class UtilsCreditData {
    private Random RANDOM = new Random();
    private final ObjectMapper mapper = new ObjectMapper();

    private final String[] tabCustomerFirstName = new String[]{
            "JAMES",
            "JOHN",
            "ROBERT",
            "MICHAEL",
            "WILLIAM",
            "DAVID",
            "RICHARD",
            "MARY",
            "PATRICIA",
            "LINDA",
            "BARBARA",
            "ELIZABETH",
            "JENNIFER",
            "MARIA",
            "SUSAN",
            "MARGARET",
            "DOROTHY",
            "LISA",
            "NANCY",
            "KAREN",
            "BETTY",
            "HELEN",
            "SANDRA",
            "DONNA",
            "CAROL",
            "RUTH",
            "SHARON"
    };
    private final String[] tabCustomerLastName = new String[]{
            "SMITH",
            "JOHNSON",
            "WILLIAMS",
            "BROWN",
            "JONES",
            "MILLER",
            "DAVI",
            "GARCIA",
            "RODRIGUEZ",
            "WILSON",
            "MARTINEZ",
            "ANDERSON",
            "TAYLOR",
            "THOMAS",
            "HERNANDEZ",
            "MOORE",
            "MARTIN",
            "JACKSON",
            "THOMPSON",
            "WHITE",
            "LOPEZ",
            "LEE",
            "GONZALEZ",
            "HARRIS",
            "CLARK",
            "LEWIS",
            "ROBINSON",
            "WALKER",
            "PEREZ",
            "HALL",
            "YOUNG",
            "ALLEN",
            "SANCHEZ",
            "WRIGHT",
            "KING",
            "SCOTT"
    };

    private final String[] tabProduct = new String[]{
            "AUTOM",
            "CONSO",
            "TRAVA",
            "ECOLO",
            "REVOL"
    };

    private final String[] tabProvider = new String[]{
            "WEBSITE",
            "AGENCE",
            "TELEPHONE",
            "PARTENAIRE 1",
            "PARTENAIRE 2"
    };

    private final String[] tabUser = new String[]{
            "u24589",
            "u10451",
            "u10852",
            "u29214"
    };

    private final String[] tabDomain = new String[]{
            "yahoo.com",
            "gmail.com",
            "gmail.com",
            "outlook.com",
            "live.com",
            "yandex.com"
    };

    private final Integer[] tabDuration = new Integer[]{
            6,
            12,
            18,
            24,
            30,
            48,
            60
    };

    private final String topic;
    private final Producer<String, String> producer;

    public UtilsCreditData(KafkaConfiguration kafkaConfiguration, KafkaUtils kafkaUtils) {
        producer = kafkaUtils.kafkaProducer();
        topic = "demo-credit";
    }

    public int random(int max){
        return RANDOM.nextInt(max);
    }

    public String getProduct(){
        return tabProduct[RANDOM.nextInt(tabProduct.length)];
    }

    public Integer getDuration(){
        return tabDuration[RANDOM.nextInt(tabDuration.length)];
    }

    public String getProvider(){
        return tabProvider[RANDOM.nextInt(tabProvider.length)];
    }

    public String getUser(){
        return tabUser[RANDOM.nextInt(tabUser.length)];
    }

    private Date addMinutesAndSecondsToTime(int minutesToAdd, int secondsToAdd, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.add(Calendar.HOUR, -3);
        cal.add(Calendar.MINUTE, minutesToAdd);
        cal.add(Calendar.SECOND, secondsToAdd);
        return cal.getTime();
    }

    private void sendToKafka(String topic, Object object) {
        try {
            String value = mapper.writeValueAsString(object);
            log.info("Sending topic {} value {}", topic, value);
            producer.send(new ProducerRecord(topic, value));
        } catch (Exception e) {
            log.error("Error sending to Kafka during generation ", e);
        }
    }

    public void generateScriptGlobalFrontEndRequest(int minute,String api, String uri, String typeRequest, String codeResponse, Integer amount,Integer creditDuration,String product,ClientData clientData,String requestId, Integer timeFront){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
        sendToKafka("front", GlobalData.builder()
                .type("front")
                .timestamp(df.format(newDate))
                .requestId(requestId)
                .timeRequestMs(timeFront.toString())
                .codeResponse(codeResponse)
                .uri(uri)
                .typeRequest(typeRequest)
                .apiName(api)
                .email(clientData.getEmail())
                .firstName(clientData.getFirstName())
                .lastName(clientData.getLastName())
                .amount(amount)
                .creditDuration(creditDuration)
                .productName(product)
                .build());
    }

    public Integer generateScriptGlobalBackendRequest(int minute, InputDataCredit inputDataCredit) {
        generateGlobalRequest(minute,inputDataCredit);
        generateWS(minute,inputDataCredit);
        generateBusinessLayer(minute,inputDataCredit);
        generateRequestDB(minute,inputDataCredit);
        return inputDataCredit.getTimeGlobal();
    }

    private void generateGlobalRequest(int minute, InputDataCredit inputDataCredit) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
        sendToKafka(inputDataCredit.getTopic(), GlobalData.builder()
                .type("backend-microservice")
                .microService(inputDataCredit.getType())
                .timestamp(df.format(newDate))
                .requestId(inputDataCredit.getRequestId())
                .timeRequestMs(inputDataCredit.getTimeGlobal().toString())
                .codeResponse(inputDataCredit.getCodeResponse())
                .uri(inputDataCredit.getUri())
                .typeRequest(inputDataCredit.getTypeRequest())
                .apiName(inputDataCredit.getApiName())
                .email(inputDataCredit.getEmail())
                .firstName(inputDataCredit.getFirstName())
                .lastName(inputDataCredit.getLastName())
                .amount(inputDataCredit.getAmount())
                .creditDuration(inputDataCredit.getCreditDuration())
                .productName(inputDataCredit.getProductName())
                .statusCredit(inputDataCredit.getStatusCredit().name())
                .build());
    }

    private void generateWS(int minute, InputDataCredit inputDataCredit) {
        if(inputDataCredit.getTimeWS()!=null && !inputDataCredit.getTimeWS().equals("")) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date newDate = addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
            sendToKafka(inputDataCredit.getTopic(), WebServiceData.builder()
                    .type("webservice")
                    .microService(inputDataCredit.getType())
                    .name(inputDataCredit.getNameWS())
                    .timestamp(df.format(newDate))
                    .requestId(inputDataCredit.getRequestId())
                    .timeRequestMs(inputDataCredit.getTimeWS().toString())
                    .codeResponse(inputDataCredit.getCodeResponseWS())
                    .uri(inputDataCredit.getUriWS())
                    .typeRequest(inputDataCredit.getTypeRequestWS())
                    .email(inputDataCredit.getEmail())
                    .firstName(inputDataCredit.getFirstName())
                    .lastName(inputDataCredit.getLastName())
                    .productName(inputDataCredit.getProductName())
                    .build());
        }
    }

    private void generateBusinessLayer(int minute, InputDataCredit inputDataCredit) {
        if(inputDataCredit.getTimeBL()!=null && !inputDataCredit.getTimeBL().equals("")) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date newDate = addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
            sendToKafka(inputDataCredit.getTopic(), BusinessLayerData.builder()
                    .type("serviceBL")
                    .microService(inputDataCredit.getType())
                    .service(inputDataCredit.getServiceBL())
                    .requestId(inputDataCredit.getRequestId())
                    .timestamp(df.format(newDate))
                    .timeRequestMs(inputDataCredit.getTimeBL().toString())
                    .email(inputDataCredit.getEmail())
                    .firstName(inputDataCredit.getFirstName())
                    .lastName(inputDataCredit.getLastName())
                    .productName(inputDataCredit.getProductName())
                    .amount(inputDataCredit.getAmount())
                    .creditDuration(inputDataCredit.getCreditDuration())
                    .build());
        }
    }

    private void generateRequestDB(int minute, InputDataCredit inputDataCredit) {
        if(inputDataCredit.getTimeDB()!=null && !inputDataCredit.getTimeDB().equals("")) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date newDate = addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
            sendToKafka(inputDataCredit.getTopic(), CreditDBData.builder()
                    .type("requestDB")
                    .microService(inputDataCredit.getType())
                    .service(inputDataCredit.getServiceBL())
                    .requestId(inputDataCredit.getRequestId())
                    .timestamp(df.format(newDate))
                    .database(inputDataCredit.getDatabase())
                    .typeRequest(inputDataCredit.getTypeDB())
                    .request(inputDataCredit.getRequestDB())
                    .timeRequestMs(inputDataCredit.getTimeDB().toString())
                    .email(inputDataCredit.getEmail())
                    .firstName(inputDataCredit.getFirstName())
                    .lastName(inputDataCredit.getLastName())
                    .amount(inputDataCredit.getAmount())
                    .creditDuration(inputDataCredit.getCreditDuration())
                    .productName(inputDataCredit.getProductName())
                    .build());
        }
    }

    public ClientData getClient() {
        String firstName =tabCustomerFirstName[RANDOM.nextInt(tabCustomerFirstName.length)];
        String lastName =tabCustomerLastName[RANDOM.nextInt(tabCustomerLastName.length)];
        String email =  firstName+ "." + lastName + "@" + tabDomain[RANDOM.nextInt(tabDomain.length)];
        return ClientData.builder().firstName(firstName).lastName(lastName).email(email).build();
    }
}
