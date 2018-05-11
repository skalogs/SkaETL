package io.skalogs.skaetl.rules.codegeneration;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class SyntaxErrorListener extends BaseErrorListener {
    @AllArgsConstructor
    class Error {
        public final int line;
        public final int charPositionInLine;
        public final Token token;
        public final String msg;

        public String toString() {
            return line + ":" + charPositionInLine + " got " + msg;
        }
    }

    private final List<Error> errors = new ArrayList<>();
    private final String formula;

    public SyntaxErrorListener(String formula) {
        this.formula = formula;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        errors.add(new Error(line, charPositionInLine, (Token) offendingSymbol, msg));
        if (!errors.isEmpty()) {
            throw buildSyntaxException();
        }
    }

    private SyntaxException buildSyntaxException() {
        String message = "\n";

        for (Error error : errors) {
            message += formula + "\n";
            message += Strings.repeat(" ", error.charPositionInLine);
            int start = error.token.getStartIndex();
            int stop = error.token.getStopIndex();

            if (start >= 0 && stop >= 0) {
                message += Strings.repeat("^", stop - start + 1) + "\n";
            }

            message += error.msg + "\r\n";
        }
        return new SyntaxException(message);
    }

    public static class SyntaxException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public SyntaxException(String msg) {
            super(msg);
        }
    }
}