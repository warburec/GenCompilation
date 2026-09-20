package component_construction.storage.factories;

import storage.storage_values.StorageValue;

/**
 * A class for objects which may produce other objects from a specified {@code StorageValue}
 * @param <T> The type of object to be produced
 */
public interface Factory <T> {
    public T produce(StorageValue<?> loadValue);
}