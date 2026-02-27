package test_aids.test_storage;

import storage.storage_value_adapters.UnsupportedValueException;
import storage.storage_values.StorageValue;
import storage.value_formatters.ValueFormatter;

public class TestIntegerValueFormatter implements ValueFormatter<Integer> {

    @Override
    public Integer format(StorageValue<?> value) throws UnsupportedValueException {
        return Integer.parseInt(value.getValue().toString());
    }

    @Override
    public StorageValue<?> parse(Integer formattedData) throws UnsupportedValueException {
        return new TestStorageValue(formattedData);
    }
    
}