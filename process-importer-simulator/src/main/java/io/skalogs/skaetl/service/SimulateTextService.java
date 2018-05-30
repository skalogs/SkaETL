package io.skalogs.skaetl.service;

import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.rules.filters.GenericFilter;
import io.skalogs.skaetl.rules.filters.RuleFilterExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static io.skalogs.skaetl.service.UtilsSimulate.generateFromValidateData;

@Slf4j
@Component
public class SimulateTextService {
    private final GenericParser genericParser;
    private final GenericTransformator genericTransformator;
    private final GenericValidator genericValidator;
    private final RuleFilterExecutor ruleExecutor;

    public SimulateTextService(RuleFilterExecutor ruleExecutor, GenericParser genericParser, GenericTransformator genericTransformator, GenericValidator genericValidator) {
        this.ruleExecutor = ruleExecutor;
        this.genericParser = genericParser;
        this.genericTransformator = genericTransformator;
        this.genericValidator = genericValidator;
    }

    public SimulateData readOutputFromText(String textInput, ProcessConsumer processConsumer) {
        String resultParsing = genericParser.apply(textInput, processConsumer);
        String resultTransformation = genericTransformator.apply(resultParsing, processConsumer);
        ValidateData item = genericValidator.process(resultTransformation, processConsumer);
        if (item.success) {
            return callFilter(textInput, item, processConsumer);
        } else {
            return generateFromValidateData(textInput, item);
        }
    }

    private SimulateData callFilter(String textInput, ValidateData item, ProcessConsumer processConsumer) {
        //going to filters
        Boolean resultFilter = processFilter(item, processConsumer);
        if (resultFilter) {
            // Ok on le garde
            item.message = "OK";
            return generateFromValidateData(textInput, item);
        } else {
            // Fail on filters
            item.statusCode = StatusCode.filter_drop_message;
            return generateFromValidateData(textInput, item);
        }
    }

    private Boolean processFilter(ValidateData item, ProcessConsumer processConsumer) {
        List<GenericFilter> genericFilters = new ArrayList<>();
        for (ProcessFilter processFilter : processConsumer.getProcessFilter()) {
            genericFilters.add(ruleExecutor.instanciate(processFilter.getName(), processFilter.getCriteria(), processFilter));
        }
        for (GenericFilter genericFilter : genericFilters) {
            if (!genericFilter.filter(item.jsonValue).getFilter()) {
                return false;
            }
        }
        return true;
    }

}
