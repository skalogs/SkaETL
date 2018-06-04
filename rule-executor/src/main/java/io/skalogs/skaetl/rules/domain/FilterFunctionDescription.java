package io.skalogs.skaetl.rules.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FilterFunctionDescription {
    private final String name;
    private final String description;
    private final String example;
}
