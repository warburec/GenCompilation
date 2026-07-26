package test_aids.storage_entities.loadableby_entities;

import component_construction.storage.dynamic_loading.LoadableBy;

public record GenericallyLoadableObject(int a) implements LoadableBy<GenericLoader<GenericallyLoadableObject>> {}

