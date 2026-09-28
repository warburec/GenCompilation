package storage.storage_values;

import java.util.*;

public class ListStorageValue extends StorageValue<List<StorageValue<?>>> {
    
    public ListStorageValue(List<StorageValue<?>> list) {
        super(list);
    }

    public ListStorageValue(StorageValue<?> ...list) {
        super(List.of(list));
    }

    @Override
    public String toString() {
        String msg = "ListStorageValue([";

        for (StorageValue<?> entry : this.value) {
            msg += entry.toString() + ", ";
        }

        msg.substring(0, msg.length() - 3); // Remove ending ", "
        
        return msg + "])";
    }
}
