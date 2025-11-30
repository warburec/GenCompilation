package test_aids.test_storage;

import storage.storage_value_adapters.UnsupportedValueException;
import storage.storage_values.StorageValue;
import storage.value_formatters.ValueFormatter;

public class TestValueFormatter implements ValueFormatter<Object> {

    @Override
    public Object format(StorageValue<?> value) throws UnsupportedValueException {
        return value;
    }

    @Override
    public StorageValue<?> parse(Object formattedData) throws UnsupportedValueException {
        return new TestStorageValue(formattedData);
    }
    
}
