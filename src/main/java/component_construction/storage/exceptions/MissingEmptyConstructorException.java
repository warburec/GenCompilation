package component_construction.storage.exceptions;

import component_construction.storage.dynamic_loading.Loader;

public class MissingEmptyConstructorException extends RuntimeException {
    
    public <T> MissingEmptyConstructorException(Class<Loader<T>> loaderClass) {
        super("The specified Loaded \"" + loaderClass.getName() + "\" is missing a required default/empty constructor");
    }
}
