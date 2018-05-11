package io.skalogs.skaetl.rules.codegeneration;

import io.skalogs.skaetl.rules.codegeneration.domain.RuleCode;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CodeGenerationUtils {

    public static void write(RuleCode ruleCode, File folder) {
        try {
            FileUtils.writeStringToFile(new File(folder, ruleCode.toFilename()), ruleCode.getJava());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
