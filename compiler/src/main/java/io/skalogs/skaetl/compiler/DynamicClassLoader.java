package io.skalogs.skaetl.compiler;

import java.util.HashMap;
import java.util.Map;

public class DynamicClassLoader extends ClassLoader {
    private final Map<String, MemoryByteCode> byteCodes = new HashMap<>();

    public DynamicClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }


    public void registerCompiledSource(MemoryByteCode byteCode) {
        byteCodes.put(byteCode.getClassName(), byteCode);
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        MemoryByteCode byteCode = byteCodes.get(name);
        if (byteCode == null) {
            return super.findClass(name);
        }

        return super.defineClass(name, byteCode.getByteCode(), 0, byteCode.getByteCode().length);
    }

    public Map<String, Class<?>> getClasses() throws ClassNotFoundException {
        Map<String, Class<?>> classes = new HashMap<>();
        for (MemoryByteCode byteCode : byteCodes.values()) {
            classes.put(byteCode.getClassName(), findClass(byteCode.getClassName()));
        }
        return classes;
    }
}