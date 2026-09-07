package test_aids.storage_entities.custom_components.loadableBy;

import component_construction.storage.dynamic_loading.LoadableBy;
import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import storage.external_interfaces.Storable;
import storage.storage_values.StorageValue;

public class LoadableByStorableCustomLexicalAnalyser implements LexicalAnalyser, LoadableBy<LoadableByCustomLexicalAnalyserLoader>, Storable {

    public StorageValue<?> loadedData;

    @Override
    public Token[] analyse(String sentence) {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }

    public void loadInternals(StorageValue<?> data) {
        loadedData = data;
    }

    @Override
    public StorageValue<?> getStorageRepresentation() {
        return loadedData;
    }
    
}
