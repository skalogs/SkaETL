package io.skalogs.skaetl.generator.credit;

import com.google.common.collect.Lists;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.service.MetricProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class UtilsCreditMetrics {

    private final MetricProcessService metricProcessService;
    private final String idProcessCreditData = "idProcessCreditData";
    private final String idProcessProviderData = "idProcessProviderData";
    private final String idProcessProductData = "idProcessProductData";
    private final String idProcessCustomerData = "idProcessCustomerData";
    private final String idProcessFrontData = "idProcessFrontData";

    public UtilsCreditMetrics(MetricProcessService metricProcessService){
        this.metricProcessService = metricProcessService;
    }

    public List<ProcessMetric> createFront(){
        List<ProcessMetric> processMetrics = new ArrayList<>();
        processMetrics.add(ProcessMetric.builder()
                .idProcess("FRONT_RESPONSE_TIME")
                .name("Average Response Time")
                .sourceProcessConsumers(Lists.newArrayList(idProcessFrontData))
                .aggFunction("AVG(timeRequestMs_long)")
                .where("type = \"front\"")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        return processMetrics;
    }


    public List<ProcessMetric> createProduct(){
        List<ProcessMetric> processMetrics = new ArrayList<>();
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
        return processMetrics;
    }

    public List<ProcessMetric> createProvider(){
        List<ProcessMetric> processMetrics = new ArrayList<>();
        processMetrics.add(ProcessMetric.builder()
                .idProcess("CODE_ERROR_PROVIDER")
                .name("Number of Error Provider")
                .sourceProcessConsumers(Lists.newArrayList(idProcessProviderData))
                .aggFunction("COUNT(*)")
                .where("codeResponse != \"200\" AND microService = \"provider\"")
                .windowType(WindowType.TUMBLING)
                .size(5)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        return processMetrics;
    }


    public List<ProcessMetric> createCustomer(){
        List<ProcessMetric> processMetrics = new ArrayList<>();
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
        return processMetrics;
    }


    public List<ProcessMetric> createMetricsCredit(){
        List<ProcessMetric> processMetrics = new ArrayList<>();
        processMetrics.add(ProcessMetric.builder()
                .idProcess("DEMAND_CREDIT_BY_PRODUCT")
                .name("Number of Demand of Credit")
                .sourceProcessConsumers(Lists.newArrayList(idProcessCreditData))
                .aggFunction("COUNT(*)")
                .where("statusCredit = \"INPROGRESS\" AND microService = \"credit\" ")
                .groupBy("productName")
                .windowType(WindowType.TUMBLING)
                .size(15)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        processMetrics.add(ProcessMetric.builder()
                .idProcess("DEMAND_CREDIT_BY_PROVIDER")
                .name("Number of Demand Credit")
                .sourceProcessConsumers(Lists.newArrayList(idProcessCreditData))
                .aggFunction("COUNT(*)")
                .where("statusCredit = \"INPROGRESS\" AND microService = \"credit\" ")
                .groupBy("provider")
                .windowType(WindowType.TUMBLING)
                .size(15)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        processMetrics.add(ProcessMetric.builder()
                .idProcess("VALIDATION_CREDIT_BY_USER")
                .name("Number of Validate Credit")
                .sourceProcessConsumers(Lists.newArrayList(idProcessCreditData))
                .aggFunction("COUNT(*)")
                .where("statusCredit = \"VALIDATE\" AND microService = \"credit\" ")
                .groupBy("user")
                .windowType(WindowType.TUMBLING)
                .size(15)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        processMetrics.add(ProcessMetric.builder()
                .idProcess("AVERAGE_AMOUNT_CREDIT_VALIDATE_BY_DURATION")
                .name("Average Amount of Validate Credit")
                .sourceProcessConsumers(Lists.newArrayList(idProcessCreditData))
                .aggFunction("AVG(amount_long)")
                .where("statusCredit = \"VALIDATE\" AND microService = \"credit\" ")
                .groupBy("creditDuration")
                .windowType(WindowType.TUMBLING)
                .size(15)
                .sizeUnit(TimeUnit.MINUTES)
                .processOutputs(Lists.newArrayList(toEsOutput()))
                .build());
        return processMetrics;
    }

    public void createAllMetrics(){
        List<ProcessMetric> processMetrics = new ArrayList<>();
        processMetrics.addAll(createMetricsCredit());
        processMetrics.addAll(createCustomer());
        processMetrics.addAll(createProduct());
        processMetrics.addAll(createProvider());
        processMetrics.addAll(createFront());
        createAndActivateMetrics(processMetrics);
    }

    private void createAndActivateMetrics(List<ProcessMetric> processMetrics) {
        for (ProcessMetric processMetric : processMetrics) {
            metricProcessService.updateProcess(processMetric);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //don't care;
        }
        for (ProcessMetric processMetric : processMetrics) {
            try {
                metricProcessService.activateProcess(processMetric);
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
