package test_aids.test_storage;

import storage.Storable;

public class TestStorable implements Storable {
    private Object value;

    public TestStorable(Object value) {
        this.value = value;
    }

    @Override
    public TestStorageValue getStorageRepresentation() {
        return new TestStorageValue(value);
    }
    
}
