package io.skalogs.skaetl.rules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class UtilsValidator {


    public static <T> T get(JsonNode jsonValue, String key) {
        if (!jsonValue.hasNonNull(key)) {
            return null;
        }
        JsonNode jsonNode = jsonValue.path(key);
        switch (jsonNode.getNodeType()) {
            case BOOLEAN:
                return (T) Boolean.valueOf(jsonNode.asBoolean());
            case NUMBER:
                if (jsonNode.isInt()) {
                    return (T) Integer.valueOf(jsonNode.asInt());
                }
                if (jsonNode.isDouble()) {
                    return (T) Double.valueOf(jsonNode.asDouble());
                }

            case STRING:
                return (T) jsonNode.asText();
            default:
                throw new IllegalArgumentException(jsonNode.getNodeType() + " type is not yet supported");

        }


    }

    // string

    public static boolean isEqualTo(String a, String b) {
        return StringUtils.equals(a, b);
    }

    public static boolean isDifferentFrom(String a, String b) {
        return !isEqualTo(a, b);
    }

    // numeric
    public static boolean isEqualTo(Number a, Number b) {
        return a == null ? false : a.equals(b);
    }

    public static boolean isDifferentFrom(Number a, Number b) {
        return !isEqualTo(a, b);
    }

    public static boolean isGreaterThan(Number a, Number b) {
        return a == null ? false : a.doubleValue() > b.doubleValue();
    }

    public static boolean isLowerThan(Number a, Number b) {
        return a == null ? false : a.doubleValue() < b.doubleValue();
    }

    public static boolean isGreaterThanOrEqual(Number a, Number b) {
        return a == null ? false : a.doubleValue() >= b.doubleValue();
    }

    public static boolean isLowerThanOrEqual(Number a, Number b) {
        return a == null ? false : a.doubleValue() <= b.doubleValue();
    }

    // dates

    public static boolean isGreaterThan(Date date, int count, TimeUnit unit) {
        return false;
    }

    public static boolean isLowerThan(Date date, int count, TimeUnit unit) {
        return false;
    }

    public static boolean isGreaterThanOrEqual(Date date, int count, TimeUnit unit) {
        return false;
    }

    public static boolean isLowerThanOrEqual(Date date, int count, TimeUnit unit) {
        return false;
    }

    public static Boolean checkPresent(JsonNode jsonValue, String... keys) {
        for (String key : keys) {
            if (!jsonValue.has(key)) {
                return false;
            }
        }
        return true;
    }

}
