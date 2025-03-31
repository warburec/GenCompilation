package storage.storage_value_adapters;

import storage.storage_values.StorageValue;

public class UnsupportedValueException extends RuntimeException {
    
    public <T> UnsupportedValueException(StorageValue<T> value) {
        super(value.getClass().toString() + " is not supported by this formatter.");
    }

    public UnsupportedValueException() {
        super(
            "The input could not be parsed by this formatter.\n" +
            "If the data was intended for this formatter, the data may be malformed." + 
            "Otherwise, Try a different formatter or develop a custom formatter for this data."
        );
    }
}