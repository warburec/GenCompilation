package test_aids.test_storage;

import storage.storage_value_adapters.UnsupportedValueException;
import storage.storage_values.StorageValue;
import storage.value_formatters.ValueFormatter;
import test_aids.exceptions.ExampleException;

public class TestStringErrorValueFormatter implements ValueFormatter<String> {

    @Override
    public String format(StorageValue<?> value) throws UnsupportedValueException {
        throw new ExampleException();
    }

    @Override
    public StorageValue<?> parse(String formattedData) throws UnsupportedValueException {
        throw new ExampleException();
    }
    
}