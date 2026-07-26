package component_construction.storage.dynamic_loading;

import java.lang.reflect.*;

import component_construction.storage.exceptions.IncorrectlyFormattedException;

/**
 * A class specifying a designated loader for the current class.
 * Loaders will be loaded using reflection.
 * @param <L> The concrete Loader class
 */
public interface LoadableBy<L extends Loader<?>> {

    @SuppressWarnings("unchecked")
    public static <T, L extends Loader<T>> Class<L> getTargetLoader(Class<? extends LoadableBy<L>> loadableByClass) throws IllegalArgumentException, IncorrectlyFormattedException {
        ParameterizedType loadableByInterface = null;

        for (Type currentInterface : loadableByClass.getGenericInterfaces()) {
            if (!(currentInterface instanceof ParameterizedType)) continue;

            ParameterizedType paramType = (ParameterizedType) currentInterface;
            if (!paramType.getRawType().equals(LoadableBy.class)) continue;
            
            loadableByInterface = paramType;
            break;
        }

        if (loadableByInterface == null)
            throw new IllegalArgumentException("Class " + loadableByClass.getName() + " does not directly implement LoadableBy.");

        Type actualTypeArgument = loadableByInterface.getActualTypeArguments()[0];

        if (actualTypeArgument instanceof ParameterizedType)
            throw new IncorrectlyFormattedException("Class " + loadableByClass.getName() + " provides a nested generic loader (" + actualTypeArgument.getTypeName() + "). Loaders must be direct, concrete Class objects.");
        
        if (!(actualTypeArgument instanceof Class))
            throw new IncorrectlyFormattedException("Class " + loadableByClass.getName() + " provides an abstract or unbound type token (" + actualTypeArgument.getTypeName() + "). The loader must be a direct, concrete Class object.");

        return (Class<L>) actualTypeArgument;
    }
    
}
