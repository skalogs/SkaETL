package io.skalogs.skaetl.rules.metrics.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AggFunctionDescription {
    private final String name;
    private final String description;
}
