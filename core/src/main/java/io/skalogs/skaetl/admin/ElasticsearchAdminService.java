package io.skalogs.skaetl.admin;


import io.skalogs.skaetl.config.ESConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
@Slf4j
public class ElasticsearchAdminService {

    private final RestHighLevelClient client;
    private final ESConfiguration esConfiguration;

    public ElasticsearchAdminService(RestHighLevelClient client, ESConfiguration esConfiguration) {
        this.client = client;
        this.esConfiguration = esConfiguration;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void prepareIndex() throws IOException {
        log.info("Creation Index");

        createAllIndex(1);
    }

    public void createAllIndex(Integer plusDay, String... listIndex) throws IOException {
        log.info("Creation Index");

        for (String index : listIndex) {
            LocalDateTime now = LocalDateTime.now();
            String indexToCreate = esConfiguration.getCustomIndexPrefix() + "-" + index + "-" + now.plusDays(plusDay).format(DateTimeFormatter.ISO_LOCAL_DATE);
            //CreateIndexRequest request = new CreateIndexRequest(indexToCreate);
            Response response = client.getLowLevelClient().performRequest("PUT", "/" + indexToCreate);
            MDC.put("creation_index_all", indexToCreate);
            log.info("creation index {} with response {}", indexToCreate, response);
            MDC.remove("creation_index_all");
        }
    }


}
