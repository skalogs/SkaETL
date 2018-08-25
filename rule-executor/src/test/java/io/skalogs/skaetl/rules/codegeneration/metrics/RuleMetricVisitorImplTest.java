package io.skalogs.skaetl.rules.codegeneration.metrics;

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

import io.skalogs.skaetl.domain.JoinType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RuleMetricVisitorImplTest {
    private final RuleMetricVisitorImpl generator = new RuleMetricVisitorImpl();

    @Test
    public void expr_int_atom() {
        assertThat(expr("1")).isEqualTo("1");
        assertThat(expr("-1")).isEqualTo("-1");
    }

    @Test
    public void expr_boolean_atom() {
        assertThat(expr("true")).isEqualTo("true");
        assertThat(expr("false")).isEqualTo("false");
    }

    @Test
    public void expr_float_atom() {
        assertThat(expr("1.1")).isEqualTo("1.1f");
        assertThat(expr("1.")).isEqualTo("1.f");
        assertThat(expr("-1.1")).isEqualTo("-1.1f");
        assertThat(expr("-1.")).isEqualTo("-1.f");
    }

    @Test
    public void expr_field_atom() {
        assertThat(expr("name")).isEqualTo("get(jsonValue,\"name\")");
        assertThat(expr("my.name")).isEqualTo("get(jsonValue,\"my.name\")");
    }

    @Test
    public void expr_string() {
        assertThat(expr("\"hola\"")).isEqualTo("\"hola\"");
    }

    @Test
    public void expr_sub_expr() {
        assertThat(expr("(1)")).isEqualTo("(1)");
    }

    @Test
    public void expr_exp_expr() {
        assertThat(expr("1^2")).isEqualTo("evaluateOperation(\"exp\",1,2)");
    }

    @Test
    public void expr_high_priority_expr() {
        assertThat(expr("1*2")).isEqualTo("evaluateOperation(\"multiply\",1,2)");
        assertThat(expr("1/2")).isEqualTo("evaluateOperation(\"divide\",1,2)");
    }

    @Test
    public void expr_low_priority_expr() {
        assertThat(expr("1+2")).isEqualTo("evaluateOperation(\"add\",1,2)");
        assertThat(expr("1 - 2")).isEqualTo("evaluateOperation(\"subtract\",1,2)");
    }

    @Test
    public void expr_comparison_expr() {
        assertThat(expr("1>2")).isEqualTo("isGreaterThan(1,2)");
        assertThat(expr("1<2")).isEqualTo("isLowerThan(1,2)");
        assertThat(expr("1<=2")).isEqualTo("isLowerThanOrEqual(1,2)");
        assertThat(expr("1>=2")).isEqualTo("isGreaterThanOrEqual(1,2)");
        assertThat(expr("1=2")).isEqualTo("isEqualTo(1,2)");
        assertThat(expr("1<>2")).isEqualTo("isDifferentFrom(1,2)");
        assertThat(expr("1!=2")).isEqualTo("isDifferentFrom(1,2)");
        assertThat(expr("1>(2)")).isEqualTo("isGreaterThan(1,(2))");
    }

    @Test
    public void expr_and_condition() {
        assertThat(expr("1 && 2")).isEqualTo("1 && 2");
        assertThat(expr("1 AND 2")).isEqualTo("1 && 2");
    }

    @Test
    public void expr_or_condition() {
        assertThat(expr("1 || 2")).isEqualTo("1 || 2");
        assertThat(expr("1 OR 2")).isEqualTo("1 || 2");
    }

    @Test
    public void expr_if_condition() {
        assertThat(expr("IF(1,2,3)")).isEqualTo("(1)?(2):(3)");
        assertThat(expr("IF(1,2,(3+4))")).isEqualTo("(1)?(2):((evaluateOperation(\"add\",3,4)))");
    }

    @Test
    public void expr_is_blank_condition() {
        assertThat(expr("IS_BLANK(a)")).isEqualTo("evaluate(\"IS_BLANK\",get(jsonValue,\"a\"))");
    }

    @Test
    public void expr_is_not_blank_condition() {
        assertThat(expr("IS_NOT_BLANK(a)")).isEqualTo("evaluate(\"IS_NOT_BLANK\",get(jsonValue,\"a\"))");
    }

    @Test
    public void expr_is_number_condition() {
        assertThat(expr("IS_NUMBER(a)")).isEqualTo("evaluate(\"IS_NUMBER\",get(jsonValue,\"a\"))");
    }

    @Test
    public void expr_contains_condition() {
        assertThat(expr("a CONTAINS(2)")).isEqualTo("evaluate(\"CONTAINS\",get(jsonValue,\"a\"),2)");
        assertThat(expr("a CONTAINS(2,3)")).isEqualTo("evaluate(\"CONTAINS\",get(jsonValue,\"a\"),2,3)");
    }

    @Test
    public void expr_regexp_condition() {
        assertThat(expr("a REGEXP(\"\\d+\")")).isEqualTo("evaluate(\"REGEXP\",get(jsonValue,\"a\"),\"\\d+\")");
        assertThat(expr("a REGEXP(\"[A-Z]*\", \"[0-1]*\")")).isEqualTo("evaluate(\"REGEXP\",get(jsonValue,\"a\"),\"[A-Z]*\",\"[0-1]*\")");
    }


    @Test
    public void expr_in_condition() {
        assertThat(expr("a IN(2)")).isEqualTo("evaluate(\"IN\",get(jsonValue,\"a\"),2)");
        assertThat(expr("a IN(2,3)")).isEqualTo("evaluate(\"IN\",get(jsonValue,\"a\"),2,3)");
    }

    @Test
    public void expr_in_subnet_condition() {
        assertThat(expr("a IN_SUBNET(\"10.12.1.0/23\")")).isEqualTo("evaluate(\"IN_SUBNET\",get(jsonValue,\"a\"),\"10.12.1.0/23\")");
    }

    @Test
    public void expr_not_in_subnet_condition() {
        assertThat(expr("NOT(a IN_SUBNET(\"10.12.1.0/23\"))")).isEqualTo("!evaluate(\"IN_SUBNET\",get(jsonValue,\"a\"),\"10.12.1.0/23\")");
        assertThat(expr("a NOT IN_SUBNET(\"10.12.1.0/23\")")).isEqualTo("!evaluate(\"IN_SUBNET\",get(jsonValue,\"a\"),\"10.12.1.0/23\")");
    }

    @Test
    public void expr_time_condition() {
        assertThat(expr("timestamp < 8 HOURS")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,HOURS)");
        assertThat(expr("timestamp < 8 HOURS")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,HOURS)");
        assertThat(expr("timestamp > 8 HOURS")).isEqualTo("isGreaterThan(get(jsonValue,\"timestamp\"),8,HOURS)");
        assertThat(expr("timestamp >= 8 HOURS")).isEqualTo("isGreaterThanOrEqual(get(jsonValue,\"timestamp\"),8,HOURS)");
        assertThat(expr("timestamp <= 8 HOURS")).isEqualTo("isLowerThanOrEqual(get(jsonValue,\"timestamp\"),8,HOURS)");
    }

    @Test
    public void expr_time_condition_seconds() {
        assertThat(expr("timestamp < 8 SECONDS")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,SECONDS)");
        assertThat(expr("timestamp < 8 S")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,SECONDS)");
        assertThat(expr("timestamp < 8 s")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,SECONDS)");
    }

    @Test
    public void expr_time_condition_minutes() {
        assertThat(expr("timestamp < 8 MINUTES")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,MINUTES)");
        assertThat(expr("timestamp < 8 M")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,MINUTES)");
        assertThat(expr("timestamp < 8 m")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,MINUTES)");
    }

    @Test
    public void expr_time_condition_hours() {
        assertThat(expr("timestamp < 8 HOURS")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,HOURS)");
        assertThat(expr("timestamp < 8 H")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,HOURS)");
        assertThat(expr("timestamp < 8 h")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,HOURS)");
    }

    @Test
    public void expr_time_condition_days() {
        assertThat(expr("timestamp < 8 DAYS")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,DAYS)");
        assertThat(expr("timestamp < 8 D")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,DAYS)");
        assertThat(expr("timestamp < 8 d")).isEqualTo("isLowerThan(get(jsonValue,\"timestamp\"),8,DAYS)");
    }


    @Test
    public void minimal() {
        RuleMetricVisitorImpl convert = convert("SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES)");
        assertThat(convert.getFrom()).isEqualTo("mytopic");
        assertThat(convert.getWindow()).isEqualTo("aggregateTumblingWindow(kGroupedStream,5,MINUTES)");
        assertThat(convert.getAggFunction()).isEqualTo("MIN");
        assertThat(convert.getAggFunctionField()).isEqualTo("duration");
        assertThat(convert.getWhere()).isNullOrEmpty();
        assertThat(convert.getGroupBy()).isNullOrEmpty();
        assertThat(convert.getHaving()).isNullOrEmpty();

    }

    @Test
    public void where() {
        RuleMetricVisitorImpl convert = convert("SELECT MIN(duration)  FROM mytopic WINDOW TUMBLING(5 MINUTES) WHERE a >= 42");
        assertThat(convert.getWhere()).isEqualTo("isGreaterThanOrEqual(get(jsonValue,\"a\"),42)");
    }

    @Test
    public void groupBy() {
        RuleMetricVisitorImpl convert = convert("SELECT MIN(duration)  FROM mytopic WINDOW TUMBLING(5 MINUTES) GROUP BY test");
        assertThat(convert.getGroupBy()).containsExactly("test");

        RuleMetricVisitorImpl convertMultiple = convert("SELECT MIN(duration)  FROM mytopic WINDOW TUMBLING(5 MINUTES) GROUP BY test,foobar");
        assertThat(convertMultiple.getGroupBy()).containsExactly("test","foobar");
    }

    @Test
    public void having() {
        RuleMetricVisitorImpl convert = convert("SELECT MIN(duration)  FROM mytopic WINDOW TUMBLING(5 MINUTES)  HAVING result > 3 TO KAFKA esulttopic");
        assertThat(convert.getHaving()).isEqualTo("result > 3");
    }

    @Test
    public void join() {
        RuleMetricVisitorImpl convert = convert("SELECT MIN(duration)  FROM mytopic WINDOW TUMBLING(5 MINUTES) JOIN mytopic2 ON (userFromA, userFromB)  WINDOWED BY 2 MINUTES");
        assertThat(convert.getJoinType()).isEqualTo(JoinType.INNER);
        assertThat(convert.getJoinFrom()).isEqualTo("mytopic2");
        assertThat(convert.getJoinKeyFromA()).isEqualTo("userFromA");
        assertThat(convert.getJoinKeyFromB()).isEqualTo("userFromB");
        assertThat(convert.getJoinWindow()).isEqualTo("JoinWindows.of(MINUTES.toMillis(2))");
        assertThat(convert.getJoinWhere()).isNullOrEmpty();
    }

    @Test
    public void joinWhere() {
        RuleMetricVisitorImpl convert = convert("SELECT MIN(duration)  FROM mytopic WINDOW TUMBLING(5 MINUTES) JOIN mytopic2 ON (userFromA, userFromB) WHERE ageCapitaine > 42 WINDOWED BY 2 MINUTES");
        assertThat(convert.getJoinFrom()).isEqualTo("mytopic2");
        assertThat(convert.getJoinKeyFromA()).isEqualTo("userFromA");
        assertThat(convert.getJoinKeyFromB()).isEqualTo("userFromB");
        assertThat(convert.getJoinWindow()).isEqualTo("JoinWindows.of(MINUTES.toMillis(2))");
        assertThat(convert.getJoinWhere()).isEqualTo("isGreaterThan(get(jsonValue,\"ageCapitaine\"),42)");
    }

    public RuleMetricVisitorImpl convert(String dsl) {
        RuleMetricVisitorImpl ruleVisitor = new RuleMetricVisitorImpl();
        ruleVisitor.visit(RuleMetricToJava.parser(dsl).parse());

        return ruleVisitor;
    }

    private String expr(String dsl) {
        return generator.visit(RuleMetricToJava.parser(dsl).expr());
    }

}
