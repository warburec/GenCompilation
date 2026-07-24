package test_aids.storage_entities.loadableby_entities;

import component_construction.storage.dynamic_loading.Loader;
import storage.storage_values.StorageValue;

public class StandardLoader implements Loader<LoadableObject> {

    @Override
    public LoadableObject produce(StorageValue<?> loadValue) {
        return new LoadableObject((int)loadValue.getValue());
    }

}
