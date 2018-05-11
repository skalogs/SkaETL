package io.skalogs.skaetl.compiler;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.*;


public class DynamicCompilerException extends RuntimeException {

    private List<Diagnostic<? extends JavaFileObject>> diagnostics;

    public DynamicCompilerException(String message, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        super(message);
        this.diagnostics = diagnostics;
    }


    public DynamicCompilerException(Throwable cause) {
        super(cause);
    }

    private List<Map<String, Object>> getErrorList() {
        List<Map<String, Object>> messages = new ArrayList<>();
        if (diagnostics != null) {
            for (Diagnostic diagnostic : diagnostics) {
                Map<String, Object> message = new HashMap<>();
                message.put("line", diagnostic.getLineNumber());
                message.put("message", diagnostic.getMessage(Locale.US));
                messages.add(message);
            }

        }
        return messages;
    }


    private String getErrors() {
        StringBuilder errors = new StringBuilder();

        for (Map<String, Object> entry : getErrorList()) {


            for (String key : entry.keySet()) {

                Object value = entry.get(key);
                if (value != null && !value.toString().isEmpty()) {
                    errors.append(key);
                    errors.append(": ");
                    errors.append(value);
                }
                errors.append(" , ");
            }

            errors.append("\n");
        }


        return errors.toString();

    }

    @Override
    public String getMessage() {
        return super.getMessage() + "\n" + getErrors();
    }


}