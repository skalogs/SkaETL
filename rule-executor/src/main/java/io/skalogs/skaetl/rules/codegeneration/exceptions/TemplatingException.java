package io.skalogs.skaetl.rules.codegeneration.exceptions;

public class TemplatingException extends RuntimeException {
    public TemplatingException(Exception e) {
        super(e);
    }
}
