package io.skalogs.skaetl.rules.metrics;

import io.skalogs.skaetl.rules.metrics.udaf.*;

import java.util.HashMap;
import java.util.Map;

public class UDAFRegistry {

    private static UDAFRegistry INSTANCE = new UDAFRegistry();

    private Map<String, Class<? extends AggregateFunction>> registry = new HashMap<>();

    private UDAFRegistry() {
        register("count", CountFunction.class);
        register("sum", SumFunction.class);
        register("min", MinFunction.class);
        register("max", MaxFunction.class);
        register("avg", AvgFunction.class);
        register("stddev", StdDevPopulationFunction.class);
        register("stddev-sample", StdDevSampleFunction.class);
        register("median", MedianFunction.class);
    }

    public void register(String name, Class<? extends AggregateFunction> aggFunctionClass) {
        registry.put(name.toLowerCase(), aggFunctionClass);
    }

    public static UDAFRegistry getInstance() {
        return INSTANCE;
    }

    public AggregateFunction get(String aggregateFunctionName) {
        try {
            Class<? extends AggregateFunction> aggFunctionClass = registry.get(aggregateFunctionName.toLowerCase());
            return aggFunctionClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
