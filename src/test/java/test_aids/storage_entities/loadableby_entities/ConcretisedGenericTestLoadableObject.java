package test_aids.storage_entities.loadableby_entities;

import component_construction.storage.dynamic_loading.LoadableBy;

public class ConcretisedGenericTestLoadableObject <T> extends StandardLoader implements GenercicTestInterface<Boolean>, LoadableBy<StandardLoader> {}