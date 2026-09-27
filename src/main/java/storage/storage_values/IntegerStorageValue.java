package storage.storage_values;

public class IntegerStorageValue extends StorageValue<Integer> {
    
    public IntegerStorageValue(Integer integer) {
        super(integer);
    }
    
    @Override
    public String toString() {
        return "IntegerStorageValue(" + this.value + ")";
    }
}
