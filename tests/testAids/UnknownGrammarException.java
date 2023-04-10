package tests.testAids;

public class UnknownGrammarException extends RuntimeException {
    public UnknownGrammarException() {
        super("The grammar requested is unsupported");
    }
}
