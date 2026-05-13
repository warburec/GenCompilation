package storage.external_interfaces;

import storage.storage_values.StorageValue;

public interface Storable {

    public StorageValue<?> getStorageRepresentation();
    
}
