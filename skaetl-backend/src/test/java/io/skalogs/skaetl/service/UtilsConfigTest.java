package io.skalogs.skaetl.service;

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

import io.skalogs.skaetl.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class UtilsConfigTest {

    private final UtilsConfig utilsConfig;

    public UtilsConfigTest() {
        utilsConfig = new UtilsConfig();
    }

    private Translate generateFilterActionTranslate(TypeFilter type) {
        return Translate.builder()
                .key("keyTranslate")
                .value("valueTranslate")
                .action(type)
                .keyPattern("MYPATTERN2")
                .build();
    }

    @Test
    public void filterAction_Date() {
        String result = utilsConfig.filterAction(generateFilterActionTranslate(TypeFilter.DATE));
        assertThat(result).isEqualTo(" date { match => [\"keyTranslate\", valueTranslate] }\n");
    }

    @Test
    public void filterAction_Add() {
        String result = utilsConfig.filterAction(generateFilterActionTranslate(TypeFilter.ADD));
        assertThat(result).isEqualTo(" mutate { add_field => {\"keyTranslate\", \"valueTranslate\"} }\n");
    }

    @Test
    public void filterAction_Grok() {
        String result = utilsConfig.filterAction(generateFilterActionTranslate(TypeFilter.GROK));
        assertThat(result).isEqualTo(" grok { match => {\"keyTranslate\" => \"MYPATTERN2\"} }\n");
    }

    private Translate generateFilterTranslate(TypeFilter type, TypeCondition tc) {
        return Translate.builder()
                .key("keyTranslate")
                .value("valueTranslate")
                .action(type)
                .keyPattern("MYPATTERN2")
                .typeCondition(tc)
                .build();
    }

    @Test
    public void filter_CondNull() {
        String result = utilsConfig.generateFilter(generateFilterTranslate(TypeFilter.GROK, null));
        assertThat(result).isEqualTo(" grok { match => {\"keyTranslate\" => \"MYPATTERN2\"} }\n");
    }

    @Test
    public void filter_CondNotNullButCheckTrue() {
        String result = utilsConfig.generateFilter(generateFilterTranslate(TypeFilter.GROK, TypeCondition.builder().condition("condition").checkPresent(true).isPresence(true).build()));
        assertThat(result).isEqualTo(" if [condition] { \n grok { match => {\"keyTranslate\" => \"MYPATTERN2\"} }\n }\n");
    }

    @Test
    public void filter_CondNotNullButCheckFalse() {
        String result = utilsConfig.generateFilter(generateFilterTranslate(TypeFilter.GROK, TypeCondition.builder().condition("condition").checkPresent(true).isPresence(false).build()));
        assertThat(result).isEqualTo(" if ![condition] { \n grok { match => {\"keyTranslate\" => \"MYPATTERN2\"} }\n }\n");
    }

    @Test
    public void input_beatTypeNull() {
        String result = utilsConfig.generateInput(ConfigurationHost.builder().typeInput(TypeInput.BEATS).port("8080").tag("example").build());
        assertThat(result).isEqualTo(" beats { port => 8080 \n tags => ['example']  \n }\n");
    }

    @Test
    public void input_beatTypeNotNull() {
        String result = utilsConfig.generateInput(ConfigurationHost.builder().typeInput(TypeInput.BEATS).port("8080").typeForced("toto").tag("example").build());
        assertThat(result).isEqualTo(" beats { port => 8080 \n tags => ['example']  \n type => \"toto\" }\n");
    }

    @Test
    public void input_tcpTypeNull() {
        String result = utilsConfig.generateInput(ConfigurationHost.builder().typeInput(TypeInput.TCP).port("8080").tag("example").build());
        assertThat(result).isEqualTo(" tcp { port => 8080 \n tags => ['example']  \n }\n");
    }

    @Test
    public void input_tcpTypeNotNull() {
        String result = utilsConfig.generateInput(ConfigurationHost.builder().typeInput(TypeInput.TCP).port("8080").typeForced("toto").tag("example").build());
        assertThat(result).isEqualTo(" tcp { port => 8080 \n tags => ['example']  \n type => \"toto\" }\n");
    }

    @Test
    public void input_tcpTypeNotNullCodecNotNull() {
        String result = utilsConfig.generateInput(ConfigurationHost.builder().typeInput(TypeInput.TCP).port("8080").typeForced("toto").codec("json").tag("example").build());
        assertThat(result).isEqualTo(" tcp { port => 8080 \n tags => ['example']  \n codec => \"json\" \n type => \"toto\" }\n");
    }

    @Test
    public void input_fileTypeNull() {
        String result = utilsConfig.generateInput(ConfigurationHost.builder().typeInput(TypeInput.FILE).path("myubberpath/toto/titi/tata").tag("example").build());
        assertThat(result).isEqualTo(" file { path => \"myubberpath/toto/titi/tata\" \n tags => ['example']  \n }\n");
    }

    @Test
    public void input_fileTypeNotNull() {
        String result = utilsConfig.generateInput(ConfigurationHost.builder().typeInput(TypeInput.FILE).path("myubberpath/toto/titi/tata").typeForced("toto").tag("example").build());
        assertThat(result).isEqualTo(" file { path => \"myubberpath/toto/titi/tata\" \n tags => ['example']  \n type => \"toto\" }\n");
    }

    @Test
    public void output_Kafka() {
        List<String> listTag = new ArrayList<>();
        listTag.add("example");
        String result = utilsConfig.generateOutput(ConfigurationOutput.builder().host("kafka").topic("mytopic").codec("json").listTag(listTag).build());
        assertThat(result).isEqualTo("if (\"example\" in [tags]){ \n kafka { \n bootstrap_servers => \"kafka:9092\" \n topic_id => \"mytopic\" \n codec => \"json\" \n }\n }\n");
    }

    @Test
    public void generateSimpleFilterWithoutCondition() {
        ConfigurationHost in = ConfigurationHost.builder().typeInput(TypeInput.TCP).port("8080").typeForced("toto").codec("json").tag("example").build();
        List<ConfigurationHost> inputList = new ArrayList<>();
        inputList.add(in);
        List<String> listTag = new ArrayList<>();
        listTag.add("example");
        List<ConfigurationOutput> outputList = new ArrayList<>();
        outputList.add(ConfigurationOutput.builder().host("kafka").port("9092").topic("mytopic").codec("json").listTag(listTag).build());
        String result = utilsConfig.generateConfig(ConfigurationLogstash.builder()
                .input(inputList)
                .output(outputList)
                .build());
        assertThat(result).isEqualTo(" input { \n tcp { port => 8080 \n tags => ['example']  \n codec => \"json\" \n type => \"toto\" }\n} \n filter {} \n output { \nif (\"example\" in [tags]){ \n kafka { \n bootstrap_servers => \"kafka:9092\" \n topic_id => \"mytopic\" \n codec => \"json\" \n }\n }\n} \n");
    }

    @Test
    public void generateSimpleFilterWithCondition() {
        ConfigurationHost in = ConfigurationHost.builder().typeInput(TypeInput.TCP).port("8080").typeForced("toto").codec("json").tag("example").build();
        List<ConfigurationHost> inputList = new ArrayList<>();
        inputList.add(in);
        List<String> listTag = new ArrayList<>();
        listTag.add("example");
        List<ConfigurationOutput> outputList = new ArrayList<>();
        outputList.add(ConfigurationOutput.builder().host("kafka").port("9092").topic("mytopic").codec("json").listTag(listTag).build());
        String result = utilsConfig.generateConfig(ConfigurationLogstash.builder()
                .input(inputList)
                .output(outputList)
                .build());
        assertThat(result).isEqualTo(" input { \n tcp { port => 8080 \n tags => ['example']  \n codec => \"json\" \n type => \"toto\" }\n} \n filter {} \n output { \nif (\"example\" in [tags]){ \n kafka { \n bootstrap_servers => \"kafka:9092\" \n topic_id => \"mytopic\" \n codec => \"json\" \n }\n }\n} \n");

    }

    @Test
    public void generateMultipleTagForOneOutput() {
        ConfigurationHost in = ConfigurationHost.builder().typeInput(TypeInput.TCP).port("8080").typeForced("toto").codec("json").tag("example").build();
        ConfigurationHost in2 = ConfigurationHost.builder().typeInput(TypeInput.TCP).port("8181").typeForced("titi").codec("json").tag("example1").build();
        List<ConfigurationHost> inputList = new ArrayList<>();
        inputList.add(in);
        inputList.add(in2);
        List<String> listTag = new ArrayList<>();
        listTag.add("example");
        listTag.add("example1");
        List<ConfigurationOutput> outputList = new ArrayList<>();
        outputList.add(ConfigurationOutput.builder().host("kafka").port("9092").topic("mytopic").codec("json").listTag(listTag).build());
        String result = utilsConfig.generateConfig(ConfigurationLogstash.builder()
                .input(inputList)
                .output(outputList)
                .build());
        assertThat(result).isEqualTo(" input { \n tcp { port => 8080 \n tags => ['example']  \n codec => \"json\" \n type => \"toto\" }\n tcp { port => 8181 \n tags => ['example1']  \n codec => \"json\" \n type => \"titi\" }\n} \n filter {} \n output { \nif (\"example\" in [tags] OR \"example1\" in [tags]){ \n kafka { \n bootstrap_servers => \"kafka:9092\" \n topic_id => \"mytopic\" \n codec => \"json\" \n }\n }\n} \n");
    }

    @Test
    public void generateMultipleTagForTwoOutput() {
        ConfigurationHost in = ConfigurationHost.builder().typeInput(TypeInput.TCP).port("8080").typeForced("toto").codec("json").tag("example").build();
        ConfigurationHost in2 = ConfigurationHost.builder().typeInput(TypeInput.TCP).port("8181").typeForced("titi").codec("json").tag("example1").build();
        List<ConfigurationHost> inputList = new ArrayList<>();
        inputList.add(in);
        inputList.add(in2);
        List<String> listTag = new ArrayList<>();
        listTag.add("example");
        List<ConfigurationOutput> outputList = new ArrayList<>();
        outputList.add(ConfigurationOutput.builder().host("kafka").port("9092").topic("mytopic").codec("json").listTag(listTag).build());
        List<String> listTag2 = new ArrayList<>();
        listTag2.add("example2");
        outputList.add(ConfigurationOutput.builder().host("kafka").port("9092").topic("topic2").codec("json").listTag(listTag2).build());
        String result = utilsConfig.generateConfig(ConfigurationLogstash.builder()
                .input(inputList)
                .output(outputList)
                .build());
        assertThat(result).isEqualTo(" input { \n tcp { port => 8080 \n tags => ['example']  \n codec => \"json\" \n type => \"toto\" }\n tcp { port => 8181 \n tags => ['example1']  \n codec => \"json\" \n type => \"titi\" }\n} \n filter {} \n output { \nif (\"example\" in [tags]){ \n kafka { \n bootstrap_servers => \"kafka:9092\" \n topic_id => \"mytopic\" \n codec => \"json\" \n }\n }\nif (\"example2\" in [tags]){ \n kafka { \n bootstrap_servers => \"kafka:9092\" \n topic_id => \"topic2\" \n codec => \"json\" \n }\n }\n} \n");
    }


}
