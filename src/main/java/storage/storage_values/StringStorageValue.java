package storage.storage_values;

public class StringStorageValue extends StorageValue<String> {
    
    public StringStorageValue(String string) {
        super(string);
    }

    @Override
    public String toString() {
        return "StringStorageValue(" + this.value + ")";
    }
    
}
