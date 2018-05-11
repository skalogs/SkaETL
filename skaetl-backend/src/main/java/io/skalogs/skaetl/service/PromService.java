package io.skalogs.skaetl.service;

import io.skalogs.skaetl.config.PrometheusConfiguration;
import io.skalogs.skaetl.domain.prometheus.PrometheusDataHack;
import io.skalogs.skaetl.web.domain.DataCapture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class PromService {

    private final PrometheusConfiguration prometheusConfiguration;

    public PromService(PrometheusConfiguration prometheusConfiguration) {
        this.prometheusConfiguration = prometheusConfiguration;
    }

    public List<DataCapture> fetchDataCapture(String term, String keyLabel, String valueLabel, int range) {
        if (StringUtils.isNotBlank(keyLabel) && StringUtils.isNotBlank(valueLabel)) {
            String argFilter = "{" + keyLabel + "=\"" + valueLabel + "\"}";
            Map<String,String> mapParam = new HashMap<>();
            mapParam.put("argFilter",argFilter);
            return convertDataCapture(callPrometheusWithArg(term, keyLabel, valueLabel, range, mapParam));
        } else {
            return convertDataCapture(callPrometheus(term, keyLabel, valueLabel, range));
        }
    }

    public Long fetchData(String term, String keyLabel, String valueLabel, int range) {
        if (StringUtils.isNotBlank(keyLabel) && StringUtils.isNotBlank(valueLabel)) {
            String argFilter = "{" + keyLabel + "=\"" + valueLabel + "\"}";
            Map<String,String> mapParam = new HashMap<>();
            mapParam.put("argFilter",argFilter);
            return computeResult(callPrometheusWithArg(term, keyLabel, valueLabel, range, mapParam));
        } else {
            return computeResult(callPrometheus(term, keyLabel, valueLabel, range));
        }
    }

    private String buildQuery(String term, String keyLabel, String valueLabel, int range) {
        //build query
        String query = "/api/v1/query_range?";
        try {
            if (StringUtils.isNotBlank(keyLabel) && StringUtils.isNotBlank(valueLabel)) {
                query = query + "query=sum(" + term + "{argFilter})&step=15s";
            } else {
                query = query + "query=" + term + "&step=15s";
            }
            LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
            query = query + "&end=" + now.toString()+"Z";
            query = query + "&start=" + now.minusMinutes(range).toString()+"Z";
        }catch (Exception e){
            log.error(""+e);
        }
        return query;
    }

    private String buildUrl() {
        return "http://" + prometheusConfiguration.getHost();
    }

    private PrometheusDataHack callPrometheus(String term, String keyLabel, String valueLabel, int range) {
        RestTemplate restTemplate = new RestTemplate();
        PrometheusDataHack result = null;
        String url = buildUrl() + buildQuery(term, keyLabel, valueLabel, range);
        try {
            result = restTemplate.getForObject(url, PrometheusDataHack.class);
        } catch (Exception e) {
            log.error("callPrometheus Error {} for url {}", e,url);
        }
        return result;
    }

    private PrometheusDataHack callPrometheusWithArg(String term, String keyLabel, String valueLabel, int range, Map<String,String> mapParam) {
        RestTemplate restTemplate = new RestTemplate();
        PrometheusDataHack result = null;
        String url = buildUrl() + buildQuery(term, keyLabel, valueLabel, range);
        try {
            result = restTemplate.getForObject(url , PrometheusDataHack.class, mapParam);
        } catch (Exception e) {
            log.error("callPrometheus Error {} for url {} arg {}", e,url,mapParam);
        }
        return result;
    }

    private Long computeResult(PrometheusDataHack prometheusDataHack) {
        Long result = 0L;
        Long init = 0L;
        if (prometheusDataHack != null && prometheusDataHack.getData() != null && prometheusDataHack.getData().getResult() != null && prometheusDataHack.getData().getResult().size() > 0 && prometheusDataHack.getData().getResult().get(0).getValues() != null && prometheusDataHack.getData().getResult().get(0).getValues().length > 0) {
            init = Long.valueOf(prometheusDataHack.getData().getResult().get(0).getValues()[0][1]);
            int last = prometheusDataHack.getData().getResult().get(0).getValues().length;
            result = Long.valueOf(prometheusDataHack.getData().getResult().get(0).getValues()[last == 0 ? last : last - 1][1]);
        }
        return result - init;
    }

    private List<DataCapture> convertDataCapture(PrometheusDataHack prometheusDataHack) {
        List<DataCapture> result = new ArrayList<>();
        Long initValue = 0L;
        if (prometheusDataHack != null && prometheusDataHack.getData() != null && prometheusDataHack.getData().getResult() != null && prometheusDataHack.getData().getResult().size() > 0 && prometheusDataHack.getData().getResult().get(0).getValues() != null && prometheusDataHack.getData().getResult().get(0).getValues().length > 0) {
            int length = prometheusDataHack.getData().getResult().get(0).getValues().length;
            initValue = Long.valueOf(prometheusDataHack.getData().getResult().get(0).getValues()[0][1]);
            for (int i = 0; i<length; i++) {
                result.add(DataCapture.builder()
                        .x(Long.valueOf(i*100/length))
                        .y(Long.valueOf(prometheusDataHack.getData().getResult().get(0).getValues()[i][1])-initValue)
                        .build());
            }
        }
        return result;
    }

}
