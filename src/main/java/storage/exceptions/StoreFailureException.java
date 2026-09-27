package storage.exceptions;

public class StoreFailureException extends RuntimeException {
    
    public StoreFailureException(Exception cause) {
        super("An error occurred when attempting a store operation.", cause);
    }
    
}