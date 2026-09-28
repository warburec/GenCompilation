package storage.exceptions;

public class NullStorageObjectException extends RuntimeException {

    public NullStorageObjectException() {
        super("The provided storage object was null. Enusre a valid Storable is provided.");
    }

}
