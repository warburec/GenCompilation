package test_aids.storage_entities.custom_components;

import component_construction.storage.dynamic_loading.ReflectivelyLoadable;
import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import storage.external_interfaces.Storable;
import storage.storage_values.StorageValue;

public class LoadableStorableCustomLexicalAnalyser implements LexicalAnalyser, ReflectivelyLoadable, Storable {
    
    public StorageValue<?> loadedData;

    @Override
    public Token[] analyse(String sentence) {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }
    
    @Override
    public void load(StorageValue<?> data) {
        loadedData = data;
    }

    @Override
    public StorageValue<?> getStorageRepresentation() {
        return loadedData;
    }
    
}
