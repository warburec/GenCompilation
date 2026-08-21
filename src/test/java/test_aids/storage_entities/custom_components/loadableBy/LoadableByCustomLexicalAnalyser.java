package test_aids.storage_entities.custom_components.loadableBy;

import component_construction.custom_components.CustomLexicalAnalyser;
import component_construction.storage.dynamic_loading.LoadableBy;
import grammar_objects.Token;
import storage.storage_values.StorageValue;

public class LoadableByCustomLexicalAnalyser implements CustomLexicalAnalyser, LoadableBy<LoadableByCustomLexicalAnalyserLoader> {

    public StorageValue<?> loadedData;

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
        loadedData = data;
    }
    
}
