package io.skalogs.skaetl.generator.credit;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class InputDataCredit {
    private String type;
    private String apiName;
    private String uri;
    private String requestId;
    private String typeRequest;
    private String codeResponse;
    private String codeResponseWS;
    private String uriWS;
    private String typeRequestWS;
    private String nameWS;
    private String serviceBL;
    private String database;
    private String typeDB;
    private String requestDB;
    private Integer timeBL;
    private Integer timeDB;
    private Integer timeWS;
    private Integer timeGlobal;
    private String email;
    private String firstName;
    private String lastName;
    private String productName;
    private String provider;
    private Integer amount;
    private Integer creditDuration;
    private String topic;
}
