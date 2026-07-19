package test_aids.storage_entities;

import component_construction.storage.dynamic_loading.Loader;
import storage.storage_values.StorageValue;

public class IncorrectlyFormedLoader implements Loader<Boolean> {
    
    public IncorrectlyFormedLoader(int a) {
        // Exists only to prevent empty constructor call
    }

    @Override
    public Boolean produce(StorageValue<?> loadValue) {
        return true;
    }

}
