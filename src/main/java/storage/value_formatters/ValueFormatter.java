package storage.value_formatters;

import storage.storage_value_adapters.UnsupportedValueException;
import storage.storage_values.StorageValue;

/**
 * An interface denoting the ability of this object to format and parse {@code StorageValues} into objects for storage
 * @param <F> The target object format
 */
public interface ValueFormatter<F> {

    /**
     * Formats the provided {@code StorageValue}
     * @param value The provided {@code StorageValue}
     * @return The formatted {@code StorageValue}
     * @throws UnsupportedValueException The {@code StorageValue} provided was not supported by this formatter
     */
    public F format(StorageValue<?> value) throws UnsupportedValueException;

    /**
     * Parses a given value into a {@code StorageValue} representation
     * @param formattedData An object containing data to be parsed
     * @return The object parsed into a {@code StorageValue}
     * @throws UnsupportedValueException The object provided was not supported by this formatter
     */
    public StorageValue<?> parse(F formattedData) throws UnsupportedValueException;
}
