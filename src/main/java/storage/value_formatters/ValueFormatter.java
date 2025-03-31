package storage.value_formatters;

import storage.storage_value_adapters.UnsupportedValueException;
import storage.storage_values.StorageValue;

public interface ValueFormatter<F> {
    public F format(StorageValue<?> value) throws UnsupportedValueException;
    public StorageValue<?> parse(F formattedData) throws UnsupportedValueException;
}
