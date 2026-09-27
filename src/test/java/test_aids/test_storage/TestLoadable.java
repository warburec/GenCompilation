package test_aids.test_storage;

import storage.external_interfaces.Loadable;
import storage.storage_values.StorageValue;

public class TestLoadable implements Loadable {
    private Object value = null;

    public Object getValue() {
        return value;
    }

    @Override
    public void load(StorageValue<?> data) {
        value = data.getValue();
    }
    
}
