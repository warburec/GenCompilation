package component_construction.storable.exceptions;

public class ReflectiveLoadFailure extends RuntimeException {

    public ReflectiveLoadFailure(String message, Throwable cause) {
        super(message, cause);
    }

}
