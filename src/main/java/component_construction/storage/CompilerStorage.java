package component_construction.storage;

import component_construction.storage.Factories.StorableCustomCompilerFactory;
import storage.Storage;
import storage.storage_values.StorageValue;

public class CompilerStorage {
    private Storage storage = new Storage();
    private StorableCustomCompilerFactory storableCustomCompilerFactory = new StorableCustomCompilerFactory();

    public void store(StorableCustomCompiler compiler) {
        //TODO: Store using Storable or StorableBy<> if defined
        storage.store(compiler);
    }

    public StorableCustomCompiler load() {
        //TODO: Load using Loadable or LoadableBy<> if defined
        StorageValue<?> storageValue = storage.load();
        return storableCustomCompilerFactory.produce(storageValue);
    }
}