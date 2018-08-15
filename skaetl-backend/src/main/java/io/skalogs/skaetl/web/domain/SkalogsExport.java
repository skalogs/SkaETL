package io.skalogs.skaetl.web.domain;

import io.skalogs.skaetl.domain.ProcessDefinition;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SkalogsExport {
    private final String version;
    private final List<ProcessDefinition> processDefinitions;
}
