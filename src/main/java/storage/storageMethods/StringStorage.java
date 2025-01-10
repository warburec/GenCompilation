package storage.storageMethods;

import storage.storageValues.StorageValue;

public class StringStorage extends StorageMethod<String> {

    @Override
    protected String defineFileExtension() {
        return ".storage.txt";
    }

    @Override
    public void store(String key, String storageType, String value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'store'");
    }

    @Override
    public StorageValue<String> load(String key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'load'");
    }
    
}
