package io.skalogs.skaetl.generator.credit;

import com.google.common.collect.Lists;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.service.MetricServiceHTTP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class UtilsCreditMetrics {

    private final MetricServiceHTTP metricServiceHTTP;
    private final String idProcessCreditData = "idProcessCreditData";
    private final String idProcessProviderData = "idProcessProviderData";
    private final String idProcessProductData = "idProcessProductData";
    private final String idProcessCustomerData = "idProcessCustomerData";
    private final String idProcessFrontData = "idProcessFrontData";

    public UtilsCreditMetrics(MetricServiceHTTP metricServiceHTTP) {
        this.metricServiceHTTP = metricServiceHTTP;
    }

    public List<ProcessMetric> createPerf(){
        List<ProcessMetric> processMetrics = new ArrayList<>();
        if (metricServiceHTTP.findById("CREDIT_RESPONSE_TIME") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("CREDIT_RESPONSE_TIME")
                    .name("Average Response Time")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessFrontData, idProcessCustomerData, idProcessProductData, idProcessProviderData, idProcessCreditData))
                    .aggFunction("AVG(timeRequestMs_long)")
                    .groupBy("type")
                    .windowType(WindowType.TUMBLING)
                    .size(1)
                    .sizeUnit(TimeUnit.MINUTES)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        if (metricServiceHTTP.findById("CREDIT_SLOW_QUERIES") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("CREDIT_SLOW_QUERIES")
                    .name("Credit - Slow Queries")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessFrontData, idProcessCustomerData, idProcessProductData, idProcessProviderData, idProcessCreditData))
                    .aggFunction("AVG(timeRequestMs_long)")
                    .where("type = \"requestDB\"")
                    .having("> 250")
                    .windowType(WindowType.TUMBLING)
                    .size(1)
                    .sizeUnit(TimeUnit.MINUTES)
                    .sourceProcessConsumersB(Lists.newArrayList(idProcessFrontData, idProcessCustomerData, idProcessProductData, idProcessProviderData, idProcessCreditData))
                    .joinType(JoinType.INNER)
                    .joinKeyFromA("requestId")
                    .joinKeyFromB("requestId")
                    .joinWindowUnit(TimeUnit.MINUTES)
                    .joinWindowSize(1)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        return processMetrics;
    }


    public List<ProcessMetric> createProduct(){
        List<ProcessMetric> processMetrics = new ArrayList<>();
        if (metricServiceHTTP.findById("CODE_ERROR_PRODUCT") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("CODE_ERROR_PRODUCT")
                    .name("Number of Error Product")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessProductData))
                    .aggFunction("COUNT(*)")
                    .where("codeResponse != \"200\" AND microService = \"product\"")
                    .windowType(WindowType.TUMBLING)
                    .size(5)
                    .sizeUnit(TimeUnit.MINUTES)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        return processMetrics;
    }

    public List<ProcessMetric> createProvider(){
        List<ProcessMetric> processMetrics = new ArrayList<>();
        if (metricServiceHTTP.findById("CODE_ERROR_PRODUCT") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("CODE_ERROR_PRODUCT")
                    .name("Number of Error Provider")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessProviderData))
                    .aggFunction("COUNT(*)")
                    .where("codeResponse != \"200\" AND microService = \"provider\"")
                    .windowType(WindowType.TUMBLING)
                    .size(5)
                    .sizeUnit(TimeUnit.MINUTES)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        return processMetrics;
    }


    public List<ProcessMetric> createCustomer(){
        List<ProcessMetric> processMetrics = new ArrayList<>();
        if (metricServiceHTTP.findById("CODE_ERROR_CUSTOMER") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("CODE_ERROR_CUSTOMER")
                    .name("Number of Error Customer")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessCustomerData))
                    .aggFunction("COUNT(*)")
                    .where("codeResponse != \"200\" AND microService = \"customer\"")
                    .windowType(WindowType.TUMBLING)
                    .size(5)
                    .sizeUnit(TimeUnit.MINUTES)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        if (metricServiceHTTP.findById("CODE_ERROR_BY_CUSTOMER") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("CODE_ERROR_BY_CUSTOMER")
                    .name("Number of Error by Customer")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessCustomerData))
                    .aggFunction("COUNT(*)")
                    .where("codeResponse != \"200\" AND microService = \"customer\"")
                    .groupBy("email_ue")
                    .windowType(WindowType.TUMBLING)
                    .size(5)
                    .sizeUnit(TimeUnit.MINUTES)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        return processMetrics;
    }


    public List<ProcessMetric> createMetricsCredit(){
        List<ProcessMetric> processMetrics = new ArrayList<>();
        if (metricServiceHTTP.findById("DEMAND_CREDIT") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("DEMAND_CREDIT")
                    .name("Number of Credit In Progress ")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessCreditData))
                    .aggFunction("COUNT(*)")
                    .where("statusCredit = \"INPROGRESS\" AND microService = \"credit\"")
                    .windowType(WindowType.TUMBLING)
                    .size(15)
                    .sizeUnit(TimeUnit.MINUTES)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        if (metricServiceHTTP.findById("DEMAND_CREDIT_BY_PRODUCT") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("DEMAND_CREDIT_BY_PRODUCT")
                    .name("Number of Credit In Progress by ProductName")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessCreditData))
                    .aggFunction("COUNT(*)")
                    .where("statusCredit = \"INPROGRESS\" AND microService = \"credit\"")
                    .groupBy("productName")
                    .windowType(WindowType.TUMBLING)
                    .size(15)
                    .sizeUnit(TimeUnit.MINUTES)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        if (metricServiceHTTP.findById("DEMAND_CREDIT_BY_PROVIDER") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("DEMAND_CREDIT_BY_PROVIDER")
                    .name("Number of Credit In Progress by Provider")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessCreditData))
                    .aggFunction("COUNT(*)")
                    .where("statusCredit = \"INPROGRESS\" AND microService = \"credit\"")
                    .groupBy("provider")
                    .windowType(WindowType.TUMBLING)
                    .size(15)
                    .sizeUnit(TimeUnit.MINUTES)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        if (metricServiceHTTP.findById("VALIDATION_CREDIT") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("VALIDATION_CREDIT")
                    .name("Number of Validate Credit")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessCreditData))
                    .aggFunction("COUNT(*)")
                    .where("statusCredit = \"VALIDATE\" AND microService = \"credit\"")
                    .windowType(WindowType.TUMBLING)
                    .size(15)
                    .sizeUnit(TimeUnit.MINUTES)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        if (metricServiceHTTP.findById("VALIDATION_CREDIT_BY_USER") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("VALIDATION_CREDIT_BY_USER")
                    .name("Number of Validate Credit by User")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessCreditData))
                    .aggFunction("COUNT(*)")
                    .where("statusCredit = \"VALIDATE\" AND microService = \"credit\"")
                    .groupBy("user")
                    .windowType(WindowType.TUMBLING)
                    .size(15)
                    .sizeUnit(TimeUnit.MINUTES)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        if (metricServiceHTTP.findById("VALIDATION_CREDIT_BY_PRODUCT") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("VALIDATION_CREDIT_BY_PRODUCT")
                    .name("Number of Validate Credit by Product")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessCreditData))
                    .aggFunction("COUNT(*)")
                    .where("statusCredit = \"VALIDATE\" AND microService = \"credit\"")
                    .groupBy("productName")
                    .windowType(WindowType.TUMBLING)
                    .size(15)
                    .sizeUnit(TimeUnit.MINUTES)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        if (metricServiceHTTP.findById("VALIDATION_CREDIT_BY_PROVIDER") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("VALIDATION_CREDIT_BY_PROVIDER")
                    .name("Number of Validate Credit by Provider")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessCreditData))
                    .aggFunction("COUNT(*)")
                    .where("statusCredit = \"VALIDATE\" AND microService = \"credit\"")
                    .groupBy("provider")
                    .windowType(WindowType.TUMBLING)
                    .size(15)
                    .sizeUnit(TimeUnit.MINUTES)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        if (metricServiceHTTP.findById("AVERAGE_AMOUNT_CREDIT_VALIDATE_BY_DURATION") == null) {
            processMetrics.add(ProcessMetric.builder()
                    .idProcess("AVERAGE_AMOUNT_CREDIT_VALIDATE_BY_DURATION")
                    .name("Average Amount of Validate Credit By Duration")
                    .sourceProcessConsumers(Lists.newArrayList(idProcessCreditData))
                    .aggFunction("AVG(amount_long)")
                    .where("statusCredit = \"VALIDATE\" AND microService = \"credit\"")
                    .groupBy("creditDuration")
                    .windowType(WindowType.TUMBLING)
                    .size(15)
                    .sizeUnit(TimeUnit.MINUTES)
                    .processOutputs(Lists.newArrayList(toEsOutput()))
                    .build());
        }
        return processMetrics;
    }

    public void createAllMetrics(){
        List<ProcessMetric> processMetrics = new ArrayList<>();
        processMetrics.addAll(createMetricsCredit());
        processMetrics.addAll(createCustomer());
        processMetrics.addAll(createProduct());
        processMetrics.addAll(createProvider());
        processMetrics.addAll(createPerf());
        createAndActivateMetrics(processMetrics);
    }

    private void createAndActivateMetrics(List<ProcessMetric> processMetrics) {
        for (ProcessMetric processMetric : processMetrics) {
            metricServiceHTTP.updateProcess(processMetric);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //don't care;
        }
        for (ProcessMetric processMetric : processMetrics) {
            try {
                metricServiceHTTP.activateProcess(processMetric);
            } catch (Exception e) {
                log.error("Error occured when activating metrics " + processMetric.getIdProcess(), e);
            }
        }
    }

    private ProcessOutput toEsOutput() {
        return ProcessOutput.builder()
                .typeOutput(TypeOutput.ELASTICSEARCH)
                .parameterOutput(ParameterOutput.builder().elasticsearchRetentionLevel(RetentionLevel.week).build())
                .build();
    }

}
