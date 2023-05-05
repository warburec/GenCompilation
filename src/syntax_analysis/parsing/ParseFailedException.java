package syntax_analysis.parsing;

public class ParseFailedException extends Exception {
    
    public ParseFailedException(Exception cause) {
        super(cause);
    }

    public ParseFailedException(Exception cause, int failedTokenIndex) {
        super("Failed at token " + failedTokenIndex + " due to " + cause.getClass().getTypeName(), cause);
    }
}
