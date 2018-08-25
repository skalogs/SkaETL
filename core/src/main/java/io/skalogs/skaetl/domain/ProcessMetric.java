package io.skalogs.skaetl.domain;

/*-
 * #%L
 * core
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

import lombok.*;
import lombok.experimental.Wither;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Wither
@Builder
@ToString
@EqualsAndHashCode(of = "idProcess")
public class ProcessMetric extends ProcessDefinition {
    private String idProcess;
    private String name;
    private String aggFunction;
    @Builder.Default
    private List<String> sourceProcessConsumers = new ArrayList<>();
    private String where;
    private String groupBy;
    private String having;

    private WindowType windowType;
    private long size;
    private TimeUnit sizeUnit;
    private long advanceBy;
    private TimeUnit advanceByUnit;

    @Builder.Default
    private List<String> sourceProcessConsumersB = new ArrayList<>();
    private JoinType joinType;
    private String joinKeyFromA;
    private String joinKeyFromB;
    private String joinWhere;
    private long joinWindowSize;
    private TimeUnit joinWindowUnit;

    @Builder.Default
    private List<ProcessOutput> processOutputs = new ArrayList<>();

    private Date timestamp;

    public String getFromTopic(){
        return "metric-" +  idProcess + "-src-a";
    }

    public String getFromTopicB(){
        return "metric-" + idProcess + "-src-b";
    }

    public String toDSL() {
        String dsl = "SELECT " +
                aggFunction +
                " FROM " + getFromTopic();

        dsl += toWindowDSL();
        if (StringUtils.isNotBlank(where)) {
            dsl += " WHERE " + where;
        }

        if (StringUtils.isNotBlank(groupBy)) {
            dsl += " GROUP BY " + groupBy;
        }
        if (StringUtils.isNotBlank(having)) {
            dsl += " HAVING result " +  having;
        }
        if (StringUtils.isNotBlank(joinKeyFromA) && StringUtils.isNotBlank(joinKeyFromB)) {
            dsl += join();
        }
        return dsl;
    }

    private String join() {
        String dsl = joinType + " JOIN " + getFromTopicB() + " ON (" + joinKeyFromA + ", " + joinKeyFromB + ") WINDOWED BY " +  joinWindowSize + " " + joinWindowUnit;
        if (StringUtils.isNotBlank(joinWhere)) {
            dsl += " WHERE " + joinWhere;
        }
        return dsl;
    }

    private String toWindowDSL() {
        switch (windowType) {
            case HOPPING:
                return " WINDOW " + windowType + '(' + size + " " + sizeUnit + ',' + advanceBy + " " + advanceByUnit + ')';
            case SESSION:
            case TUMBLING:
                return " WINDOW " + windowType + '(' + size + " " + sizeUnit + ')';
            default:
                throw new IllegalArgumentException("Unsupported window type " + windowType);
        }
    }

}
