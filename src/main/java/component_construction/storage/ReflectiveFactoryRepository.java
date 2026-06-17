package component_construction.storage;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import component_construction.storage.Factories.Factory;
import storage.storage_values.StorageValue;

public class ReflectiveFactoryRepository <T> {
    protected Map<String, Factory<T>> factories = new HashMap<>();
    protected Class<T> innerType;

    public ReflectiveFactoryRepository(Class<T> innerType) {
        this.innerType = innerType;
    }

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

    /**
     * Instantiates the named entity using the given value. 
     * <br><br>
     * If no factory exists for the named entity, the name will be treated as a class name as expected by {@code Class.forName(...)}.
     * The named class must include a constructor of the form {@code public NamedClass(StorageValue<?> state) { ... }} in order to be instantiated.
     * @param name The entity name
     * @param loadValue The StorageValue to be loaded into the specified entity
     * @return The constructed entity
     * @throws ClassNotFoundException
     */
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

        try {
            return clazz
                .asSubclass(innerType)
                .getDeclaredConstructor(StorageValue.class)
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
            throw new RuntimeException(e); //TODO: Combine into more descriptive/useful exception per fail case
        }
    }
}
