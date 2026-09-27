package component_construction.storable.exceptions;

import storage.external_interfaces.Storable;

public class NonStorableComponentException extends RuntimeException {
    
    public NonStorableComponentException(Object component) {
        super("The selected component \"" + component.getClass().getName() + "\" must implement " + Storable.class.getSimpleName());
    }

}
