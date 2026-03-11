package storage.exceptions;

public class FormatParseException extends RuntimeException {
    
    public FormatParseException(Exception cause) {
        super("An error occurred when attempting to parse formatted data.", cause);
    }
    
}
