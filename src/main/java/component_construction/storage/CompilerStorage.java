package component_construction.storage;

import component_construction.storage.factories.StorableCustomCompilerFactory;
import storage.Storage;
import storage.storage_values.StorageValue;

public class CompilerStorage {
    protected Storage storage = new Storage();
    protected StorableCustomCompilerFactory storableCustomCompilerFactory = new StorableCustomCompilerFactory();

    public void store(StorableCustomCompiler compiler) {
        storage.store(compiler);
    }

    public StorableCustomCompiler load() {
        StorageValue<?> storageValue = storage.load();
        return storableCustomCompilerFactory.produce(storageValue);
    }

    // TODO: Include other load variations
}