package component_construction.storage;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import component_construction.storage.Factories.Factory;
import storage.storage_values.StorageValue;

public class FactoryRepository <T> {
    protected Map<String, Factory<T>> factories = new HashMap<>();

    /**
     * 
     * @param name
     * @param generator
     * @return This object for method chaining
     */
    public FactoryRepository<T> addFactory(String name, Factory<T> generator) {
        factories.put(name, generator);
        return this;
    }

    public boolean containsKey(String name) {
        return factories.containsKey(name);
    }

    /**
     * Instantiates the named entity using the given value.
     * @param name The entity name
     * @param loadValue The StorageValue to be loaded into the specified entity
     * @return The constructed entity
     * @throws ClassNotFoundException
     */
    public T instantiate(String name, StorageValue<?> loadValue) throws ClassNotFoundException {
        return factories
            .get(name)
            .produce(loadValue);
    }
}
