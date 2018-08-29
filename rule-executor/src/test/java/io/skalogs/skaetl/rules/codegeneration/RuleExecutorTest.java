package io.skalogs.skaetl.rules.codegeneration;

/*-
 * #%L
 * rule-executor
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

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.ProcessFilter;
import io.skalogs.skaetl.rules.codegeneration.filters.RuleFilterToJava;
import io.skalogs.skaetl.rules.filters.GenericFilter;
import io.skalogs.skaetl.rules.filters.RuleFilterExecutor;
import io.skalogs.skaetl.rules.functions.FunctionRegistry;
import io.skalogs.skaetl.rules.repository.FilterFunctionDescriptionRepository;
import org.junit.Test;
import org.mockito.Mockito;

import static io.skalogs.skaetl.rules.JSONUtils.createJsonNode;
import static org.assertj.core.api.Assertions.assertThat;


public class RuleExecutorTest {
    private final FilterFunctionDescriptionRepository filterFunctionDescriptionRepositoryMock = Mockito.mock(FilterFunctionDescriptionRepository.class);
    private final FunctionRegistry functionRegistry = new FunctionRegistry(filterFunctionDescriptionRepositoryMock);
    private RuleFilterExecutor ruleExecutor = new RuleFilterExecutor(new RuleFilterToJava(functionRegistry),functionRegistry);

    @Test
    public void shouldCompile() {
        GenericFilter myCompileFilter = ruleExecutor.instanciate("myCompileFilter", "key1 >= 3", ProcessFilter.builder().build());
        String test = "{\"key1\": 100}";
        JsonNode jsonObject = createJsonNode(test);
        assertThat(myCompileFilter.filter(jsonObject).getFilter()).isTrue();
    }

}
