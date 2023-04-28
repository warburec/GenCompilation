package tests.test_aids;

public class UnknownGrammarException extends RuntimeException {
    public UnknownGrammarException() {
        super("The grammar requested is unsupported");
    }
}
