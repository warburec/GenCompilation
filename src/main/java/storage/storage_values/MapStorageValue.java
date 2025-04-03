package storage.storage_values;

import java.util.Map;
import java.util.Map.Entry;

public class MapStorageValue extends StorageValue<Map<String, StorageValue<?>>> {
    
    public MapStorageValue(Map<String, StorageValue<?>> map) {
        super(map);
    }

    @Override
    public String toString() {
        String msg = "MapStorageValue({";

        for (Entry<String, StorageValue<?>> entries : this.value.entrySet()) {
            msg += entries.getKey();
            msg += ":";
            msg += entries.getValue().toString() + ", ";
        }

        msg.substring(0, msg.length() - 3); // Remove ending ", "
        
        return msg + "})";
    }
    
}