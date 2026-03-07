package storage.exceptions;

public class FormatterException extends RuntimeException {
    
    public FormatterException(Exception cause) {
        super("An error occurred when attempting to format data for storage.", cause);
    }
    
}
