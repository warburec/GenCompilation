package component_construction.storage.exceptions;

public class ReflectiveLoadFailure extends RuntimeException {

    public ReflectiveLoadFailure(String message, Throwable cause) {
        super(message, cause);
    }

}
