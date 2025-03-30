package storage.storageValues;

public abstract class StorageValue<T> {
    private T value;

    public StorageValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        StorageValue<?> other = (StorageValue<?>) obj;
        if (value == null) return other.value == null;
        return value.equals(other.value);
    }
}
