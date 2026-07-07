package component_construction.storage.dynamic_loading;

import java.lang.reflect.InvocationTargetException;
import component_construction.storage.factories.Factory;

public interface Loader<T> extends Factory<T> {

    public static <T> Loader<T> construct(Class<Loader<T>> loaderClass) {
        try {
            return loaderClass
                .getConstructor()
                .newInstance();
        } 
        catch (
            InstantiationException 
            | IllegalAccessException 
            | IllegalArgumentException
            | InvocationTargetException 
            | NoSuchMethodException 
            e
        ) {
            throw new RuntimeException(); //TODO: Custom error
        }
    }

}
