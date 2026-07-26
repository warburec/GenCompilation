package test_aids.storage_entities.loadableby_entities;

import component_construction.storage.dynamic_loading.Loader;
import storage.storage_values.StorageValue;

public class GenericLoader <T> implements Loader<T> {

    private GenericLoader() {
        // Exists only to prevent empty constructor call
    }

    @Override
    @SuppressWarnings("unchecked")
    public T produce(StorageValue<?> loadValue) {
        return (T)new GenericallyLoadableObject((int)loadValue.getValue());
    }
    
}
