package component_construction.storage.factories;

import storage.storage_values.StorageValue;

public interface Factory <T> {
    public T produce(StorageValue<?> loadValue);
}