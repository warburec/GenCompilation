package storage.exceptions;

public class FormattingException extends RuntimeException {
    
    public FormattingException(Exception cause) {
        super("An error occurred when attempting to format data for storage.", cause);
    }
    
}
