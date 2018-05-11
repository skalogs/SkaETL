package io.skalogs.skaetl.compiler;

import lombok.extern.slf4j.Slf4j;

import javax.tools.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@Slf4j
public class DynamicJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private final PackageInternalsFinder finder;

    private final DynamicClassLoader classLoader;
    private final List<MemoryByteCode> byteCodes = new ArrayList<>();


    public DynamicJavaFileManager(JavaFileManager fileManager, DynamicClassLoader classLoader) {
        super(fileManager);
        this.classLoader = classLoader;

        finder = new PackageInternalsFinder(classLoader);
    }


    @Override
    public JavaFileObject getJavaFileForOutput(
            JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
            throws IOException {

        for (MemoryByteCode byteCode : byteCodes) {
            if (byteCode.getClassName().equals(className)) {
                return byteCode;
            }
        }

        try {
            MemoryByteCode innerClass = new MemoryByteCode(className);
            byteCodes.add(innerClass);
            classLoader.registerCompiledSource(innerClass);
            return innerClass;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        return null;
    }


    @Override
    public ClassLoader getClassLoader(JavaFileManager.Location location) {
        return classLoader;
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        if (file instanceof CustomJavaFileObject) {
            return ((CustomJavaFileObject) file).binaryName();
        } else { // if it's not CustomJavaFileObject, then it's coming from standard file manager - let it handle the file
            return super.inferBinaryName(location, file);
        }
    }

    @Override
    public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
        if (location == StandardLocation.PLATFORM_CLASS_PATH) { // let standard manager hanfle
            return super.list(location, packageName, kinds, recurse);
        } else if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
            if (packageName.startsWith("java")) { // a hack to let standard manager handle locations like "java.lang" or "java.util". Prob would make sense to join results of standard manager with those of my finder here
                return super.list(location, packageName, kinds, recurse);
            } else { // app specific classes are here
                return finder.find(packageName);
            }
        }
        return Collections.emptyList();

    }

}

