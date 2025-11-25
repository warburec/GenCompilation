package storage;

import storage.storage_values.StorageValue;

public interface Loadable {
    
    public void load(StorageValue<?> data);

}
