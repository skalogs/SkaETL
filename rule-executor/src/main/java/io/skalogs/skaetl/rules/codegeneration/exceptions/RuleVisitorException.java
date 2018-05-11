package io.skalogs.skaetl.rules.codegeneration.exceptions;

public class RuleVisitorException extends RuntimeException {
    public RuleVisitorException(Exception e) {
        super(e);
    }
}