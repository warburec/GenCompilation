package storage.storage_values;

public abstract class StorageValue<T> {
    protected T value;

    public StorageValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public abstract String toString();

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
