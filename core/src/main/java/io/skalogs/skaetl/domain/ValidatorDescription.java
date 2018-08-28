package io.skalogs.skaetl.domain;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "name")
public class ValidatorDescription {
    private final String name;
    private final String description;
}
