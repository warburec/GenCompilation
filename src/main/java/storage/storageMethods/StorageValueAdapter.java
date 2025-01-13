package storage.storageMethods;

import storage.storageValues.StorageValue;

public interface StorageValueAdapter<T> {
    
    public T retreiveValue(StorageValue<T> storageValue);
    public StorageValue<T> packageValue(T value);

}
