package component_construction.storage.exceptions;

import storage.external_interfaces.Storable;

public class NonStorableComponentException extends RuntimeException {
    
    public NonStorableComponentException(Object component) {
        super("The selected component \"" + component.getClass().getName() + "\" must implement " + Storable.class.getSimpleName());
    }

}
