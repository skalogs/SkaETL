package io.skalogs.skaetl.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.prometheus.client.Gauge;
import io.skalogs.skaetl.config.ESConfiguration;
import io.skalogs.skaetl.domain.ConfEsSkalogs;
import io.skalogs.skaetl.domain.ConfigurationLogstash;
import io.skalogs.skaetl.domain.StatusConfig;
import io.skalogs.skaetl.web.domain.ConfLogstashWeb;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class ConfService {

    public HashMap<String, ConfigurationLogstash> map = new HashMap<>();
    public static final Gauge conf = Gauge.build()
            .name("skaetl_nb_configuration")
            .help("nb Configuration")
            .labelNames("status")
            .register();
    private final String INDEX_STORAGE = "skalogsconf";
    private final RestHighLevelClient restHighLevelClient;
    private final ESConfiguration esConfiguration;
    private final UtilsConfig utilsConfig;

    @PostConstruct
    public void init() {
        //load from ES the configuration
        SearchRequest searchRequest = new SearchRequest(INDEX_STORAGE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            if (searchResponse.getFailedShards() > 0) {
                treatError(searchResponse);
            } else {
                treatResponse(searchResponse);
            }
        } catch (Exception e) {
            log.error("Error during call ES for load Configuration at startup {}", e);
        }
    }

    private void treatError(SearchResponse searchResponse) {
        log.error("Pwoblem when load configuration From ES");
        for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
            log.error(failure.toString());
        }
    }

    private void treatResponse(SearchResponse searchResponse) {
        for (SearchHit searchHit : searchResponse.getHits()) {
            String res = searchHit.getSourceAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ConfEsSkalogs conf = objectMapper.readValue(res, ConfEsSkalogs.class);
                map.put(conf.getConfigurationLogstash().getIdConfiguration(),conf.getConfigurationLogstash());
                log.info("Add configuration {}",conf.getConfigurationLogstash());
            }catch(Exception e){
                log.error("Pwoblem during parsing {}",e);
            }
        }
    }

    public ConfService(RestHighLevelClient restHighLevelClient, ESConfiguration esConfiguration, UtilsConfig utilsConfig) {
        this.restHighLevelClient = restHighLevelClient;
        this.esConfiguration = esConfiguration;
        this.utilsConfig = utilsConfig;
    }

    public void createConfiguration(ConfigurationLogstash configurationLogstash) {
        ISO8601DateFormat df = new ISO8601DateFormat();
        Date newDate = new Date();
        configurationLogstash.setTimestamp(df.format(newDate));
        configurationLogstash.setIdConfiguration(UUID.randomUUID().toString());
        configurationLogstash.setStatusConfig(StatusConfig.INIT);
        map.put(configurationLogstash.getIdConfiguration(), configurationLogstash);
    }

    public void deleteConfiguration(String id) {
        deactiveConfiguration(id);
        map.remove(id);
    }

    public void editConfiguration(ConfigurationLogstash configurationLogstash) {
        ISO8601DateFormat df = new ISO8601DateFormat();
        Date newDate = new Date();
        configurationLogstash.setTimestamp(df.format(newDate));
        map.put(configurationLogstash.getIdConfiguration(), configurationLogstash);
    }

    public ConfigurationLogstash getConfiguration(String idConfiguration) {
        return map.get(idConfiguration);
    }

    public List<ConfigurationLogstash> findAll() {
        return map.values().stream().collect(toList());
    }

    private void updateStat() {
        conf.labels(StatusConfig.ACTIVE.name()).set(map.values().stream()
                .filter(configurationLogstash -> configurationLogstash.getStatusConfig() == StatusConfig.ACTIVE)
                .count());
        conf.labels(StatusConfig.ERROR.name()).set(map.values().stream()
                .filter(configurationLogstash -> configurationLogstash.getStatusConfig() == StatusConfig.ERROR)
                .count());
        conf.labels(StatusConfig.DISABLE.name()).set(map.values().stream()
                .filter(configurationLogstash -> configurationLogstash.getStatusConfig() == StatusConfig.DISABLE)
                .count());
    }

    public void activeConfiguration(String idConfiguration) {
        ConfigurationLogstash cl = map.get(idConfiguration);
        if (cl != null) {
            try {
                callAddES(cl);
                cl.setStatusConfig(StatusConfig.ACTIVE);
            } catch (Exception e) {
                log.error("Exception {}", e);
                cl.setStatusConfig(StatusConfig.ERROR);
            }
            updateStat();
        }
    }

    public void deactiveConfiguration(String idConfiguration) {
        ConfigurationLogstash cl = map.get(idConfiguration);
        if (cl != null) {
            try {
                callRemoveES(cl);
                cl.setStatusConfig(StatusConfig.DISABLE);

            } catch (Exception e) {
                log.error("Exception {}", e);
                cl.setStatusConfig(StatusConfig.ERROR);
            }
            updateStat();
        }
    }

    private void callRemoveES(ConfigurationLogstash cl) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX_STORAGE, "doc", cl.getIdEs());
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
        if (deleteResponse != null) {
            deleteResponse.status();
            deleteResponse.toString();
            cl.setIdEs(null);
        }
    }

    private void callAddES(ConfigurationLogstash cl) throws IOException {
        BulkRequest bulk = new BulkRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        String fluxJson = objectMapper.writeValueAsString(ConfEsSkalogs.builder().configurationLogstash(cl).pipeline(StringEscapeUtils.escapeJava(utilsConfig.generateConfig(cl))).build());
        bulk.add(new IndexRequest(INDEX_STORAGE)
                .type("doc")
                .id(cl.getIdConfiguration())
                .source(fluxJson, XContentType.JSON));
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulk);
        if (bulkResponse.getItems().length == 1) {
            cl.setIdEs(bulkResponse.getItems()[0].getId());
        } else {
            log.error("Problem with return ES {}", bulkResponse);
        }
    }

    public ConfLogstashWeb generate(String idConfiguration) {
        ConfigurationLogstash cl = map.get(idConfiguration);
        if (cl != null) {
            return ConfLogstashWeb.builder()
                    .commandLogstash(generateCommandConfigClient(cl))
                    .confLogstash(generateConfClient(cl))
                    .build();
        } else {
            return ConfLogstashWeb.builder()
                    .commandLogstash("No configuration")
                    .confLogstash("No configuration")
                    .build();
        }
    }

    public String generateConfClient(ConfigurationLogstash cl){
        if(cl.statusCustomConfiguration){
            return cl.getCustomConfiguration();
        }else{
            return utilsConfig.generateConfig(cl);
        }
    }

    public String generateCommandConfigClient(ConfigurationLogstash cl) {
        StringBuilder sb = new StringBuilder();
        sb.append("./bin/logstash -e \"$(curl 'http://etl-backend:8090/esConfiguration/fetch?");
        sb.append("env=" + cl.getConfData().getEnv() + "&");
        sb.append("category=" + cl.getConfData().getCategory() + "&");
        sb.append("apiKey=" + cl.getConfData().getApiKey() + "&");
        sb.append("hostname=<YOUR_HOSTNAME>')\"");

        return sb.toString();
    }

}
