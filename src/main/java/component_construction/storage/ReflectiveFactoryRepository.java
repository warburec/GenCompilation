package component_construction.storage;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import component_construction.storage.Factories.Factory;
import helper_objects.TypeReference;
import storage.storage_values.StorageValue;

public class ReflectiveFactoryRepository <T> {
    protected Map<String, Factory<T>> factories = new HashMap<>();

    /**
     * 
     * @param name
     * @param generator
     * @return This object for method chaining
     */
    public ReflectiveFactoryRepository<T> addFactory(String name, Factory<T> generator) {
        factories.put(name, generator);
        return this;
    }

    public T instantiate(String name, StorageValue<?> loadValue) throws ClassNotFoundException {
        if (!factories.containsKey(name)) {
            // Assume name is the name of a class with type T
            return createInstanceOf(name, loadValue);
        }

        return factories
            .get(name)
            .produce(loadValue);
    }

    /**
     * 
     * @param <T>
     * @param className Must include the package definition e.g "com.example.ExampleClass"
     * @param state
     * @return
     * @throws Exception
     */
    private T createInstanceOf(String className, StorageValue<?> state) throws ClassNotFoundException {
        Class<?> clazz;
        
        try {
            clazz = Class.forName(className);
        } 
        catch (ClassNotFoundException e) {
            throw e; //TODO: More descriptive exception
        }

        Class<T> expectedClass = TypeReference.<T>instantiate().getContainedClass();

        //TODO: Call default constructor for loading (Must be made as a standard if not registering a factory)
        try {
            return clazz
                .asSubclass(expectedClass)
                .getDeclaredConstructor(state.getClass())
                .newInstance(state);
        } 
        catch (
            InstantiationException 
            | IllegalAccessException 
            | IllegalArgumentException 
            | InvocationTargetException
            | NoSuchMethodException 
            | SecurityException 
            e
        ) {
            throw new RuntimeException(e); //TODO: Combine into more descriptive/useful exception
        }
    }
}
