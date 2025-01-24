package storage.valueFormatters;

import java.util.*;
import storage.storageValues.*;

public class ValueToStringFormatter implements ValueFormatter<String> {

    protected static final String KEY_VALUE_SEPERATOR = " : ";

    boolean storageUsed = false;
    List<String> existingKeys = new ArrayList<String>();

    @Override
    public String format(StorageValue<?> value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'format'");
    }

    @Override
    public StorageValue<?> parse(String formattedData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parse'");
    }
    
}
