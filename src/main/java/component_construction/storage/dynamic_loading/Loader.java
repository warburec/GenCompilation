package component_construction.storage.dynamic_loading;

import java.lang.reflect.InvocationTargetException;

import component_construction.storage.exceptions.MissingEmptyConstructorException;
import component_construction.storage.factories.Factory;

public interface Loader<T> extends Factory<T> {

    public static <T> Loader<T> construct(Class<Loader<T>> loaderClass) throws MissingEmptyConstructorException, IllegalArgumentException {
        try {
            return loaderClass
                .getConstructor()
                .newInstance();
        }
        catch (NoSuchMethodException e) {
            throw new MissingEmptyConstructorException(loaderClass);
        }
        catch (
            InstantiationException 
            | IllegalAccessException 
            | IllegalArgumentException
            | InvocationTargetException
            e
        ) {
            throw new IllegalArgumentException(e);
        }
    }

}
