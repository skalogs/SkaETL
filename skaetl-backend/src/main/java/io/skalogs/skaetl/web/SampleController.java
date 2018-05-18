package io.skalogs.skaetl.web;

import com.google.common.collect.Lists;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.service.MetricProcessService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.OK;

@Component
@RequestMapping("/sample")
@AllArgsConstructor
public class SampleController {

    private final MetricProcessService metricProcessService;

    @GetMapping("/generate")
    @ResponseStatus(OK)
    public void setupSecurityRules() {
        sshMetricRules();
        firewallMetricRules();
        databaseMetricRules();
        proxyMetricRules();
        joinMetricRules();
    }

    private void joinMetricRules() {
        List<ProcessMetric> processMetrics = new ArrayList<>();
        processMetrics.add(ProcessMetric.builder()
                .idProcess("DATABASE_CONNECTION_FROM_LOCALHOST")
                .name("Database connection from localhost joined with SSH connection")
                .aggFunction("count()")
                .where("status = \"OK\" AND remoteIp = \"127.0.0.1\"")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .joinKeyFromA("databaseIp")
                .joinKeyFromB("destIp")
                .joinWindowSize(15)
                .joinWindowUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        createProcessMetrics(processMetrics);
    }

    private void proxyMetricRules() {
        List<ProcessMetric> processMetrics = new ArrayList<>();

        processMetrics.add(ProcessMetric.builder()
                .idProcess("PROXY_NB_REQUEST_NON_2XX")
                .name("Proxy nb request non 2XX")
                .aggFunction("count()")
                .where("httpCode < 200 AND httpCode > 300")
                .groupBy("remoteIp")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("PROXY_NB_REQUEST_SAME_IP_DIFFERENT_SESSION_ID")
                .name("Proxy nb request with same ip and different session id")
                .aggFunction("count()")
                .groupBy("remoteIp,cookieSession")
                .having("> 1")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("PROXY_NB_UPLOAD_REQUEST_PER_SRC_IP")
                .name("Proxy nb upload request per src ip")
                .aggFunction("count()")
                .where("httpWord IN (\"PUT\",\"POST\",\"PATCH\")")
                .groupBy("remoteIp")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("PROXY_AVG_REQUEST_SIZE_PER_USER")
                .name("Proxy average request size per user")
                .aggFunction("avg(requestSize)")
                .where("httpWord IN (\"PUT\",\"POST\",\"PATCH\")")
                .groupBy("user")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("PROXY_NB_DELETE_REQUEST_PER_SRC_IP")
                .name("Proxy nb delete request per src ip")
                .aggFunction("count()")
                .where("httpWord = \"DELETE\"")
                .groupBy("remoteIp")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("PROXY_NB_DELETE_REQUEST_PER_URL")
                .name("Proxy nb delete request per url")
                .aggFunction("count()")
                .where("httpWord = \"DELETE\"")
                .groupBy("url")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("PROXY_NB_SLOW_REQUEST_PER_SRC_IP")
                .name("Proxy nb slow request per src ip")
                .aggFunction("count()")
                .where("globalRequestTime > 10")
                .groupBy("remoteIp")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("PROXY_NB_SLOW_CONNECTION_PER_SRC_IP")
                .name("Proxy nb slow connection per src ip")
                .aggFunction("count()")
                .where("cnxRequestTime > 10")
                .groupBy("remoteIp")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("PROXY_NB_BIG_REQUEST_SIZE_PER_IP")
                .name("Proxy nb request with big request size per src ip")
                .aggFunction("count()")
                .where("requestSize > 10")
                .groupBy("remoteIp")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("PROXY_NB_BIG_RESPONSE_SIZE_PER_IP")
                .name("Proxy nb request with big response size per src ip")
                .aggFunction("count()")
                .where("responseSize > 10")
                .groupBy("remoteIp")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("PROXY_NB_REQUEST_URL_IN_BL_PER_IP")
                .name("Proxy nb request with url in black list per src ip")
                .aggFunction("count()")
                .where("uri CONTAINS (\"/login\",\"/logout\",\"/audit\",\"/admin\")")
                .groupBy("remoteIp")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("PROXY_NB_REQUEST_URL_IN_BL_PER_URL")
                .name("Proxy nb request with url in black list per url")
                .aggFunction("count()")
                .where("uri CONTAINS (\"/login\",\"/logout\",\"/audit\",\"/admin\")")
                .groupBy("url")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        createProcessMetrics(processMetrics);
    }

    private void databaseMetricRules() {
        List<ProcessMetric> processMetrics = new ArrayList<>();
        processMetrics.add(ProcessMetric.builder()
                .idProcess("DATABASE_NB_CONNECTION_FAIL")
                .name("Database nb connection fail")
                .aggFunction("count()")
                .where("status = \"KO\"")
                .groupBy("remoteIp")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("DATABASE_NB_INSERT_PER_SRC_IP")
                .name("Database nb insert per src ip")
                .aggFunction("count()")
                .where("typeRequest = \"INSERT\"")
                .groupBy("remoteIp")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("DATABASE_NB_CONNECTION_FAIL_PER_DB_NAME")
                .name("Database nb connection per database name")
                .aggFunction("count()")
                .where("status = \"KO\"")
                .groupBy("databaseName")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        createProcessMetrics(processMetrics);
    }

    private void firewallMetricRules() {
        List<ProcessMetric> processMetrics = new ArrayList<>();

        processMetrics.add(ProcessMetric.builder()
                .idProcess("FIREWALL_BLOCK_PER_DEST")
                .name("Firewall block by destination")
                .aggFunction("count()")
                .where("status = \"BLOCKED\"")
                .groupBy("destIp")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        processMetrics.add(ProcessMetric.builder()
                .idProcess("FIREWALL_BLOCK_PER_DEST_WITH_SPECIFIC_PORTS")
                .name("Firewall block by destination on specific ports")
                .aggFunction("count()")
                .where("status = \"BLOCKED\" AND destPort in (25,80)")
                .groupBy("destIp")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("FIREWALL_BLOCK_PER_DEST_IN_SENSIBLE_SUBNET")
                .name("Firewall block by destination in sensible subnet")
                .aggFunction("count()")
                .where("status = \"BLOCKED\" AND destIp IN_SUBNET(\"10.15.8.1/16\")")
                .groupBy("destIp")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        createProcessMetrics(processMetrics);
    }

    private void sshMetricRules() {
        List<ProcessMetric> processMetrics = new ArrayList<>();
        processMetrics.add(ProcessMetric.builder()
                .idProcess("SSH_CONNECTION_PER_SRC_IP")
                .name("SSH connexion per source IP")
                .aggFunction("count()")
                .groupBy("clientIp")
                .windowType(WindowType.TUMBLING)
                .size(10)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("SSH_CONNECTION_FAIL_PER_SRC_IP")
                .name("SSH connexion fail per source IP")
                .aggFunction("count()")
                .where("status = \"KO\"")
                .groupBy("clientIp")
                .windowType(WindowType.TUMBLING)
                .size(10)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());

        processMetrics.add(ProcessMetric.builder()
                .idProcess("SSH_CONNECTION_FAIL_PER_DEST_IP")
                .name("SSH connexion fail per dest IP")
                .aggFunction("count()")
                .where("status = \"KO\"")
                .groupBy("serverIp")
                .windowType(WindowType.TUMBLING)
                .size(10)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        createProcessMetrics(processMetrics);
    }

    private void createProcessMetrics(List<ProcessMetric> processMetrics) {
        for (ProcessMetric processMetric : processMetrics) {
            metricProcessService.updateProcess(processMetric);
        }
    }

    private ProcessOutput toEsOutput() {
        return ProcessOutput.builder()
                .typeOutput(TypeOutput.ELASTICSEARCH)
                .parameterOutput(ParameterOutput.builder().elasticsearchRetentionLevel(RetentionLevel.week).build())
                .build();
    }
}
