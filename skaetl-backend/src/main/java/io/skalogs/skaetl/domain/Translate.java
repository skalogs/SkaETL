package io.skalogs.skaetl.domain;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Translate {
    public String key;
    public String value;
    public TypeFilter action;
    public TypeCondition typeCondition;
    public String keyPattern;
    public String idTranslateConfiguration;
}
