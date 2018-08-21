package io.skalogs.skaetl.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ErrorData {
    public String timestamp;
    public String errorReason;
    @JsonProperty("errorMessage_text")
    public String errorMessage;
    public String typeValidation;
    public String message;
}


