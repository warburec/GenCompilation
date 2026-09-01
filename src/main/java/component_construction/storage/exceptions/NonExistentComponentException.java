package component_construction.storage.exceptions;

public class NonExistentComponentException extends RuntimeException {
    
    public NonExistentComponentException(String attemptedComponentClassName, Throwable cause) {
        super(
            "The component class \"" + attemptedComponentClassName + "\" could not be found or does not exist. Check the provided component name correctly references the intended class.",
            cause
        );
    }
}
