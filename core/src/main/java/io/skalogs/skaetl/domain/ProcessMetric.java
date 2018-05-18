package io.skalogs.skaetl.domain;

import lombok.*;
import lombok.experimental.Wither;
import org.apache.commons.lang.StringUtils;

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
        String dsl = " JOIN " + getFromTopicB() + " ON (" + joinKeyFromA + ", " + joinKeyFromB + ") WINDOWED BY " +  joinWindowSize + " " + joinWindowUnit;
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
