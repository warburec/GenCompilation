package storage.external_interfaces;

import storage.storage_values.StorageValue;

public interface Loadable {
    
    public void load(StorageValue<?> data);

}
