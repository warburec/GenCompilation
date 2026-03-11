package storage.exceptions;

public class LoadFailureException extends RuntimeException {

    public LoadFailureException(Exception cause) {
        super("An error occurred when attempting a load operation.", cause);
    }

}