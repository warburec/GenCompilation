package lexical_analysis;

public class LexicalError extends RuntimeException {
    public LexicalError(String message, int lineNum, int columnNum) {
        super(message + " at line:" + lineNum + ", column:" + columnNum + ".");
    }

    public LexicalError(String message, int tokenNum) {
        super(message + " at token:" + tokenNum + ".");
    }

    public LexicalError(String message, int lineNum, int columnNum, String additionalInfo) {
        super(message + " at line:" + lineNum + ", column:" + columnNum + ". " + additionalInfo);
    }

    public LexicalError(String message, int tokenNum, String additionalInfo) {
        super(message + " at token:" + tokenNum  + ". " + additionalInfo);
    }
}
