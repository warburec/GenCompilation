package test_aids.storage_entities.custom_components;

import code_generation.CodeGenerator;
import storage.external_interfaces.Storable;
import storage.storage_values.*;
import syntax_analysis.parsing.ParseState;

public class TestStorableCustomCodeGenerator implements CodeGenerator, Storable {

    @Override
    public String generate(ParseState parseRoot) {
        throw new UnsupportedOperationException("Unimplemented method 'generate'");
    }

    @Override
    public StorageValue<?> getStorageRepresentation() {
        return new StringStorageValue("TestStorableCustomCodeGenerator");
    }
    
}
