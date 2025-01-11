package storage.helper_objects;

public class DuplicateKeyException extends RuntimeException {
    
    public <T> DuplicateKeyException(T key) {
        super("Duplicated definition of the key \'" + key.toString() + " was provided.");
    }
}
