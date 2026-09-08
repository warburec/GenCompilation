package storage.storage_values;

public class NullStorageValue extends StorageValue<Void> {
    
    public NullStorageValue() {
        super(null);
    }

    @Override
    public String toString() {
        return "NullStorageValue()";
    }
    
}
