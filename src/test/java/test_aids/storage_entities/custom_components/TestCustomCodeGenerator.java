package test_aids.storage_entities.custom_components;

import component_construction.custom_components.CustomCodeGenerator;
import storage.storage_values.StorageValue;
import syntax_analysis.parsing.ParseState;

public class TestCustomCodeGenerator implements CustomCodeGenerator {

    @Override
    public String generate(ParseState parseRoot) {
        throw new UnsupportedOperationException("Unimplemented method 'generate'");
    }

    @Override
    public StorageValue<?> getStorageRepresentation() {
        throw new UnsupportedOperationException("Unimplemented method 'getStorageRepresentation'");
    }

    @Override
    public void load(StorageValue<?> data) {
        throw new UnsupportedOperationException("Unimplemented method 'load'");
    }
    
}
