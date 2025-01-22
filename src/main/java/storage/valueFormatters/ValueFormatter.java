package storage.valueFormatters;

import storage.storageValues.StorageValue;

public interface ValueFormatter<F> {
    public F format(StorageValue<?> value);
    public StorageValue<?> parse(F formattedData);
}
