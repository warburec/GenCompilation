package storage.valueFormatters;

import storage.storageValueAdapters.UnsupportedValueException;
import storage.storageValues.StorageValue;

public interface ValueFormatter<F> {
    public F format(StorageValue<?> value) throws UnsupportedValueException;
    public StorageValue<?> parse(F formattedData) throws UnsupportedValueException;
}
