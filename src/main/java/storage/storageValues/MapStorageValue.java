package storage.storageValues;

import java.util.Map;

public class MapStorageValue extends StorageValue<Map<String, StorageValue<?>>> {
    
    public MapStorageValue(Map<String, StorageValue<?>> map) {
        super(map);
    }
    
}