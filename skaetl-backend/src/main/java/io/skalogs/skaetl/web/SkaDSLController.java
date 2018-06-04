package io.skalogs.skaetl.web;

import io.skalogs.skaetl.rules.domain.FilterFunctionDescription;
import io.skalogs.skaetl.rules.functions.FunctionRegistry;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dsl")
@AllArgsConstructor
public class SkaDSLController {

    @GetMapping("/filterFunctions")
    public List<FilterFunctionDescription> filterFunctions() {
        return FunctionRegistry.getInstance().filterFunctions();

    }
}
