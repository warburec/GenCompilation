package storage.external_interfaces;

import storage.storage_values.StorageValue;

/**
 * An interface denoting that this object's state can be fetched
 */
public interface Storable {

    /**
     * Gets the storage representation of this object
     * @return A {@code StoraageValue} representing the storable state of this object
     */
    public StorageValue<?> getStorageRepresentation();
    
}
