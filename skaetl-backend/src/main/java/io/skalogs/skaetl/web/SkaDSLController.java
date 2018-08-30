package io.skalogs.skaetl.web;

/*-
 * #%L
 * skaetl-backend
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import io.skalogs.skaetl.domain.ParserDescription;
import io.skalogs.skaetl.domain.TransformatorDescription;
import io.skalogs.skaetl.domain.ValidatorDescription;
import io.skalogs.skaetl.repository.ParserDescriptionRepository;
import io.skalogs.skaetl.repository.TransformatorDescriptionRepository;
import io.skalogs.skaetl.repository.ValidatorDescriptionRepository;
import io.skalogs.skaetl.rules.domain.FilterFunctionDescription;
import io.skalogs.skaetl.rules.metrics.domain.AggFunctionDescription;
import io.skalogs.skaetl.rules.metrics.repository.AggFunctionDescriptionRepository;
import io.skalogs.skaetl.rules.repository.FilterFunctionDescriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/dsl")
@AllArgsConstructor
public class SkaDSLController {

    private final FilterFunctionDescriptionRepository filterFunctionDescriptionRepository;
    private final AggFunctionDescriptionRepository aggFunctionDescriptionRepository;

    private final ParserDescriptionRepository parserDescriptionRepository;
    private final TransformatorDescriptionRepository transformatorDescriptionRepository;
    private final ValidatorDescriptionRepository validatorDescriptionRepository;


    @GetMapping("/filterFunctions")
    public List<FilterFunctionDescription> filterFunctions() {
        return filterFunctionDescriptionRepository.findAll();
    }

    @GetMapping("/aggFunctions")
    public List<AggFunctionDescription> aggFunctions() {
        return aggFunctionDescriptionRepository.findAll();
    }


    @GetMapping("parsers")
    public List<ParserDescription> parsers() {
        return parserDescriptionRepository.findAll();
    }

    @GetMapping("transformators")
    public List<TransformatorDescription> transformators() {
        return transformatorDescriptionRepository.findAll();
    }

    @GetMapping("validators")
    public List<ValidatorDescription> validators() {
        return validatorDescriptionRepository.findAll();
    }

    @GetMapping("clear-descriptions")
    @ResponseStatus(OK)
    public void clearExtentionPointDescritions() {
        parserDescriptionRepository.deleteAll();
        transformatorDescriptionRepository.deleteAll();
        validatorDescriptionRepository.deleteAll();
        filterFunctionDescriptionRepository.deleteAll();
        aggFunctionDescriptionRepository.deleteAll();
    }
}
