package storage.external_interfaces;

import storage.storage_values.StorageValue;

/**
 * An interface denoting the state of this object may be loaded from a specified {@code StorageValue}
 */
public interface Loadable {
    
    /**
     * Loads the given state into this object
     * @param data The {@code StorageValue} state to be loaded
     */
    public void load(StorageValue<?> data);

}
