package test_aids.storage_entities;

import component_construction.storage.dynamic_loading.Loader;
import storage.storage_values.StorageValue;

public class TestObjectLoader implements Loader<TestObject> {

    @Override
    public TestObject produce(StorageValue<?> loadValue) {
        return new TestObject((int)loadValue.getValue());
    }
    
}
