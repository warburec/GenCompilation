package storage.valueFormatters;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

import storage.FileOperations;
import storage.helper_objects.DuplicateKeyException;
import storage.storageValues.*;

public class StringStorage implements ValueFormatter<String> {

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
