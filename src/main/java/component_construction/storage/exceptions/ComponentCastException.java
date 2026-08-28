package component_construction.storage.exceptions;

public class ComponentCastException extends RuntimeException {
    
    public ComponentCastException(String componentClassName, String expectedType) {
        super("The provided component class \"" + componentClassName + "\" could not be cast to the expected type \"" + expectedType + "\". Ensure \"" + componentClassName + "\" implements \"" + expectedType + "\".");
    }
}
