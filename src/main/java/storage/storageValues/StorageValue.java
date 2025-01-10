package storage.storageValues;

public abstract class StorageValue<T> {
    private T value;

    public StorageValue(T value) {
        this.value = value;
    }
}
