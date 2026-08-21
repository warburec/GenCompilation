package component_construction.storage.factories;

import java.util.*;

import storage.storage_values.StorageValue;

//TODO: Remove?
public class FactoryRepository <T> {
    protected Map<String, Factory<T>> factories = new HashMap<>();

    /**
     * Adds a factory to this repository
     * @param name The name of the factory
     * @param factory The factory
     * @return This object for method chaining
     */
    public FactoryRepository<T> addFactory(String name, Factory<T> factory) {
        factories.put(name, factory);
        return this;
    }

    /**
     * Checks if this repository contains a factory for a given key
     * @param name The name/key of the factory
     * @return Whether or not this repository contains the specified key
     */
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
