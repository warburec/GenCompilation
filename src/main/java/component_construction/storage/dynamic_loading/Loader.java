package component_construction.storage.dynamic_loading;

import java.lang.reflect.InvocationTargetException;

import component_construction.storage.exceptions.MissingEmptyConstructorException;
import component_construction.storage.factories.Factory;

/**
 * An interface defining the ability construct objects of the specified type
 * @param <T> The type of object to be constructed
 */
public interface Loader<T> extends Factory<T> {

    /**
     * Constructs a new object of the specified {@code Loader}
     * @param <T> The type of object constructable by the {@code Loader}
     * @param loaderClass The class of the {@code Loader} to be reflectively constructed
     * @return An object of the specified {@code Loader}
     * @throws MissingEmptyConstructorException The {@code Loader} did not contain the required empty constructor
     * @throws IllegalArgumentException The specified {@code Loader} could not be constructed as it is either abstract or the constructor is inaccessible
     */
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
