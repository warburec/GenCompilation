package test_aids.storage_entities.custom_components;

import component_construction.custom_components.CustomLexicalAnalyser;
import grammar_objects.Token;
import storage.storage_values.StorageValue;

public class TestCustomLexicalAnalyser implements CustomLexicalAnalyser {

    @Override
    public Token[] analyse(String sentence) {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
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
