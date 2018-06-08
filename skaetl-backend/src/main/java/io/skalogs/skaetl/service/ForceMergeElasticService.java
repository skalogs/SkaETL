package io.skalogs.skaetl.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.config.ESConfiguration;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ForceMergeElasticService {
    private final RestHighLevelClient client;
    private final ESConfiguration esConfiguration;

    public ForceMergeElasticService(RestHighLevelClient client, ESConfiguration esConfiguration) {
        this.client = client;
        this.esConfiguration = esConfiguration;
    }

    @Scheduled(cron = "* 0/30 * * * ?")
    public void forceMergeAllIndexMetricsAndReferential(){
        log.info("start forceMerge All Index Metrics And Referential");
        try {
            Response response = client.getLowLevelClient().performRequest("GET", "_cat/indices?format=json");
            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                if(entity!=null) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    JsonNode jsonNode = JSONUtils.getInstance().parse(responseBody);
                    if(jsonNode!=null) {
                        jsonNode.findValues("index").stream()
                                .filter(itemJsonNode -> itemJsonNode.asText().contains("metrics") || itemJsonNode.asText().contains("referential"))
                                .forEach(itemJsonNode -> forceMerge(itemJsonNode.asText()));
                    }
                }
            }else{
                log.error("search all indices not response 200 {}",response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
           log.error("Error during search all indices {}",e);
        }
    }

    private void forceMerge(String index) {
        log.info("forceMerge index {}",index);
        try {
            Response response = client.getLowLevelClient().performRequest("POST", index+"/_forcemerge?only_expunge_deletes=true&max_num_segments=100&flush=true");
            log.info("response after forceMerge {}",EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            log.error("Error during forceMerge request for {] msg {}",index,e);
        }
    }

   /* private void forceMergeNextFeature(String index){
        log.info("Force Merge");
        ForceMergeRequest request = new ForceMergeRequest(index);
        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        request.maxNumSegments(100);
        request.onlyExpungeDeletes(true);
        request.flush(true);

        ForceMergeResponse forceMergeResponse = client.indices().forceMerge(request);
    }*/
}
