package io.skalogs.skaetl.compiler;

import lombok.extern.slf4j.Slf4j;

import javax.tools.*;
import java.util.*;

@Slf4j
public class DynamicCompiler {
    private final JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
    private final StandardJavaFileManager standardFileManager;
    private final List<String> options = new ArrayList<>();
    private final DynamicClassLoader dynamicClassLoader;

    private final Collection<JavaFileObject> compilationUnits = new ArrayList<>();
    private final List<Diagnostic<? extends JavaFileObject>> errors = new ArrayList<>();
    private final List<Diagnostic<? extends JavaFileObject>> warnings = new ArrayList<>();


    public DynamicCompiler() {
        standardFileManager = javaCompiler.getStandardFileManager(null, null, null);

        options.add("-Xlint:unchecked");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        dynamicClassLoader = new DynamicClassLoader(classLoader);

    }


    public void addSource(String className, String source) {
        addSource(new StringSource(className, source));
    }


    public void addSource(JavaFileObject javaFileObject) {
        compilationUnits.add(javaFileObject);
    }

    public Map<String, Class<?>> build() {

        errors.clear();
        warnings.clear();

        JavaFileManager fileManager = new DynamicJavaFileManager(standardFileManager, dynamicClassLoader);


        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        JavaCompiler.CompilationTask task = javaCompiler.getTask(null, fileManager, collector, options, null, compilationUnits);

        try {

            if (!compilationUnits.isEmpty()) {
                boolean result = task.call();

                if (!result || collector.getDiagnostics().size() > 0) {

                    for (Diagnostic<? extends JavaFileObject> diagnostic : collector.getDiagnostics()) {
                        switch (diagnostic.getKind()) {
                            case NOTE:
                            case MANDATORY_WARNING:
                            case WARNING:
                                warnings.add(diagnostic);
                                break;
                            case OTHER:
                            case ERROR:
                            default:
                                errors.add(diagnostic);
                                break;
                        }

                    }


                    if (!errors.isEmpty()) {
                        throw new DynamicCompilerException("Compilation Error", errors);
                    }
                }
            }

            return dynamicClassLoader.getClasses();
        } catch (ClassFormatError | ClassNotFoundException e) {
            throw new DynamicCompilerException(e);
        } finally {
            compilationUnits.clear();

        }


    }


    private List<String> diagnosticToString(List<Diagnostic<? extends JavaFileObject>> diagnostics) {

        List<String> diagnosticMessages = new ArrayList<>();

        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            diagnosticMessages.add("line: " + diagnostic.getLineNumber() + ", message: " + diagnostic.getMessage(Locale.US));
        }

        return diagnosticMessages;

    }


    public List<String> getErrors() {
        return diagnosticToString(errors);
    }

    public List<String> getWarnings() {
        return diagnosticToString(warnings);
    }

    public ClassLoader getClassLoader() {
        return dynamicClassLoader;
    }
}