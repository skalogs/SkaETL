package io.skalogs.skaetl.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@Slf4j
public class ESConfiguration {
    private String host;
    private String port;
    private String clusterName;
    private String serviceElasticsearchUsername;
    private String serviceElasticsearchPassword;
    private boolean sniff = true;
    private Integer clientTransportPingTimeout;
    private String customIndexPrefix;
    private Integer connectionTimeout = 30;

    @Bean
    public RestHighLevelClient elasticsearchRestConnection(ESConfiguration esConfiguration) {
        String auth = esConfiguration.getServiceElasticsearchUsername() + ":" + esConfiguration.getServiceElasticsearchPassword();
        String authB64;
        try {
            authB64 = Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Impossible encoding for user " + esConfiguration.getServiceElasticsearchUsername() + " and password " + esConfiguration.getServiceElasticsearchPassword() + " msg " + e);
        }
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(esConfiguration.getHost(), Integer.valueOf(esConfiguration.getPort()), "http"));
        Header[] defaultHeaders = new Header[]{
                new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
                new BasicHeader("cluster.name", esConfiguration.getClusterName()),
                new BasicHeader(HttpHeaders.AUTHORIZATION, "Basic " + authB64)
        };
        builder.setDefaultHeaders(defaultHeaders);
        builder.setMaxRetryTimeoutMillis(esConfiguration.getClientTransportPingTimeout() * 1000);
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                return requestConfigBuilder
                        .setConnectTimeout(esConfiguration.getConnectionTimeout() * 1000)
                        .setSocketTimeout(esConfiguration.getClientTransportPingTimeout() * 1000);
            }
        });
        return new RestHighLevelClient(builder);
    }
}
