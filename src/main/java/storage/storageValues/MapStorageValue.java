package storage.storageValues;

import java.util.Map;

public class MapStorageValue <T> extends StorageValue<Map<String, StorageValue<T>>> {
    
    public MapStorageValue(Map<String, StorageValue<T>> map) {
        super(map);
    }
    
}