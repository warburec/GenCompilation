package test_aids.test_storage;

import storage.storage_values.StorageValue;

public class TestStorageValue extends StorageValue<Object> {

    public TestStorageValue(Object value) {
        super(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
    
}
