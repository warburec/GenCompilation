package test_aids.test_storage;

import storage.storage_value_adapters.UnsupportedValueException;
import storage.storage_values.StorageValue;
import storage.value_formatters.ValueFormatter;

public class TestValueFormatter implements ValueFormatter<String> {

    @Override
    public String format(StorageValue<?> value) throws UnsupportedValueException {
        return value.getValue().toString();
    }

    @Override
    public StorageValue<?> parse(String formattedData) throws UnsupportedValueException {
        return new TestStorageValue(formattedData);
    }
    
}
