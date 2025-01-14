package storage.storageValueAdapters;

import storage.storageValues.StorageValue;

/**
 * An interface that defines an adapter converting StorageValues to a chosen representation for use within a StorageMethod
 */
public interface StorageValueAdapter <T, E> {
    
    public E retreiveValue(StorageValue<T> storageValue);
    public StorageValue<T> packageValue(E value);

}
