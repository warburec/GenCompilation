package component_construction.storable.exceptions;

import component_construction.storable.dynamic_loading.Loader;

public class MissingEmptyConstructorException extends RuntimeException {
    
    public <T> MissingEmptyConstructorException(Class<Loader<T>> loaderClass) {
        super("The specified Loaded \"" + loaderClass.getName() + "\" is missing a required default/empty constructor");
    }
}
