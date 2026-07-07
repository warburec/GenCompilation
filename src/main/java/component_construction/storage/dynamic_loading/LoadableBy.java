package component_construction.storage.dynamic_loading;

import java.lang.reflect.*;

/**
 * A class specifying a designated loader for the current class.
 * Loaders will be loaded using reflection.
 * @param <T>
 */
public interface LoadableBy<L extends Loader<?>> {

    @SuppressWarnings("unchecked")
    public static <T> Class<Loader<T>> getTargetLoader(Class<LoadableBy<Loader<T>>> loadableByClass) {
        for (Type currentInterface : loadableByClass.getGenericInterfaces()) {
            if (!(currentInterface instanceof ParameterizedType)) continue;

            ParameterizedType paramType = (ParameterizedType) currentInterface;
            
            if (!paramType.getRawType().equals(LoadableBy.class)) continue;
            
            Type actualTypeArgument = paramType.getActualTypeArguments()[0];
            
            //TODO: Scrutinise below code, along with warning supression

            // If T is a standard class/interface, return it
            if (actualTypeArgument instanceof Class)
                return (Class<Loader<T>>) actualTypeArgument;
            // Handle nested generic loaders (e.g., MyLoader<String>)
            else if (actualTypeArgument instanceof ParameterizedType)
                return (Class<Loader<T>>) ((ParameterizedType) actualTypeArgument).getRawType();
        }

        throw new IllegalArgumentException("Class " + loadableByClass.getName() + " does not directly implement LoadableBy with a concrete generic type.");
    }

}
