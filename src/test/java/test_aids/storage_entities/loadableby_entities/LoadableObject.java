package test_aids.storage_entities.loadableby_entities;

import storage.dynamic_loading.LoadableBy;

public record LoadableObject(int a) implements LoadableBy<StandardLoader> {}

