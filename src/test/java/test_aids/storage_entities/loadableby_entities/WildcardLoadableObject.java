package test_aids.storage_entities.loadableby_entities;

import component_construction.storable.dynamic_loading.*;

public record WildcardLoadableObject(int a) implements LoadableBy<Loader<?>> {}

