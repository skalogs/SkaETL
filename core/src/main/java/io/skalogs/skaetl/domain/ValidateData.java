package io.skalogs.skaetl.domain;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class ValidateData {
    @Getter
    public boolean success;
    public StatusCode statusCode;
    @Builder.Default
    public String message = "";
    public String value;
    public JsonNode jsonValue;
    public Date timestamp;
    public TypeValidation typeValidation;
    //Mandatory
    public String type;
    //Mandatory
    public String project;
    @Builder.Default
    public List<StatusCode> errorList = new ArrayList<>();


}