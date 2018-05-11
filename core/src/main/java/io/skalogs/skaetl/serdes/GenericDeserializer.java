package io.skalogs.skaetl.serdes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GenericDeserializer<T> implements Deserializer<T> {

    private final Class<T> clazz;

    public GenericDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void configure(Map<String, ?> map, boolean b) {

    }

    public T deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T objToReturn = null;
        try {
            objToReturn = mapper.readValue(bytes, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objToReturn;
    }

    public void close() {

    }
}
