package storage.valueFormatters;

import java.util.*;

import storage.storageValueAdapters.UnsupportedValueException;
import storage.storageValues.*;

public class ValueToStringFormatter implements ValueFormatter<String> {

    protected static final String KEY_VALUE_SEPERATOR = " : ";

    boolean storageUsed = false;
    List<String> existingKeys = new ArrayList<String>();

    @Override
    public String format(StorageValue<?> value) throws UnsupportedValueException {
        if (value instanceof StringStorageValue) return formatValue((StringStorageValue)value);
        if (value instanceof IntegerStorageValue) return formatValue((IntegerStorageValue)value);
        //TODO: Add more value types and ensure types are differentiatable from their stored formats
        throw new UnsupportedValueException(value);
    }

    @Override
    public StorageValue<?> parse(String formattedData) throws UnsupportedValueException {
        //TODO
        throw new UnsupportedValueException();
    }


    private String formatValue(StringStorageValue value) {
        return value.getValue();
    }

    private String formatValue(IntegerStorageValue value) {
        return String.valueOf(value.getValue());
    }
    
}
