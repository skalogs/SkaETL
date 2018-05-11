package io.skalogs.skaetl.rules.codegeneration;

import io.skalogs.skaetl.rules.functions.FunctionRegistry;
import org.apache.commons.lang.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RuleToJava {
    public static String nullSafePredicate(String lambda) {
        if (StringUtils.isBlank(lambda) || "null".equals(lambda)) {
            return "true";
        }
        return lambda;
    }

    public static String nullSafeFunction(String lambda) {
        if (StringUtils.isBlank(lambda) || "null".equals(lambda)) {
            return null;
        }
        return "(obj) -> " + lambda;
    }

    public static String timeunit(String text) {
        switch (text) {
            case "SECONDS":
            case "S":
            case "s":
                return "SECONDS";
            case "MINUTES":
            case "M":
            case "m":
                return "MINUTES";
            case "HOURS":
            case "H":
            case "h":
                return "HOURS";
            case "DAYS":
            case "D":
            case "d":
                return "DAYS";
            default:
                throw new RuntimeException(text + " is not a timeunit");
        }
    }


    public static String lowPriorityOperation(String operation, String expr1, String expr2) {
        switch (operation) {
            case "+":
                return twoArgsFunction("add", expr1, expr2);
            case "-":
                return twoArgsFunction("subtract", expr1, expr2);
            default:
                throw new IllegalStateException(operation + " is not supported");
        }
    }

    public static String highPriorityOperation(String operation, String expr1, String expr2) {
        switch (operation) {
            case "/":
                return twoArgsFunction("divide", expr1, expr2);
            case "*":
                return twoArgsFunction("multiply", expr1, expr2);
            default:
                throw new IllegalStateException(operation + " is not supported");
        }
    }

    public static String comparisonMethod(String operation, String expr1, String expr2) {
        String arguments = "(" + expr1 + "," + expr2 + ")";
        switch (operation) {
            case "=":
            case "==":
                return "isEqualTo" + arguments;
            case "!=":
            case "<>":
                return "isDifferentFrom" + arguments;
            case ">":
                return "isGreaterThan" + arguments;
            case "<":
                return "isLowerThan" + arguments;
            case ">=":
                return "isGreaterThanOrEqual" + arguments;
            case "<=":
                return "isLowerThanOrEqual" + arguments;
            default:
                throw new IllegalArgumentException("Could not handle " + operation);
        }
    }

    public static String timeComparisonMethod(String operation, String fieldname, String time, String timeunit) {
        String arguments = "(" + fieldname + "," + time + "," + timeunit + ")";
        switch (operation) {
            case ">":
                return "isGreaterThan" + arguments;
            case "<":
                return "isLowerThan" + arguments;
            case ">=":
                return "isGreaterThanOrEqual" + arguments;
            case "<=":
                return "isLowerThanOrEqual" + arguments;
            case "=":
            case "==":
            case "!=":
            case "<>":
            default:
                throw new IllegalArgumentException("Could not handle " + operation);
        }
    }

    public static String exp(String expr1, String expr2) {
        return twoArgsFunction("exp", expr1, expr2);
    }

    public static String and(String expr1, String expr2) {
        return expr1 + " && " + expr2;
    }

    public static String or(String expr1, String expr2) {
        return expr1 + " || " + expr2;
    }

    public static String not(String expr) {
        return "!" + expr;
    }

    public static String ifCondition(String condition, String thenClause, String elseClause) {
        return "(" + condition + ")?(" + thenClause + "):(" + elseClause + ")";
    }

    public static String oneArgCondition(String functionName, String fieldValue) {
        if (FunctionRegistry.getInstance().getRuleFunction(functionName) == null) {
            throw new IllegalArgumentException("Unknown function " + functionName);
        }
        return "evaluate(" + functionName + "," + fieldValue + ")";
    }

    public static String twoArgsFunction(String function, String expr1, String expr2) {
        return function + "(" + expr1 + "," + expr2 + ")";
    }

    public static String varArgCondition(String functionName, String fieldValue, String args) {
        if (FunctionRegistry.getInstance().getRuleFunction(functionName) == null) {
            throw new IllegalArgumentException("Unknown function " + functionName);
        }

        return "evaluate(" + functionName + "," + fieldValue + "," + args + ")";
    }

    public static String toCamelCase(String input) {
        String sanitizedInput = org.apache.commons.lang3.StringUtils.stripAccents(input);
        return Stream.of(sanitizedInput.split("[^a-zA-Z0-9]"))
                .map(v -> v.substring(0, 1).toUpperCase() + v.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }
}
