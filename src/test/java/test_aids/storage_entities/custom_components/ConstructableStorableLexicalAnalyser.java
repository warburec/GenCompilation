package test_aids.storage_entities.custom_components;

import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import storage.external_interfaces.Storable;
import storage.storage_values.StorageValue;

public class ConstructableStorableLexicalAnalyser implements LexicalAnalyser, Storable {
    
    public StorageValue<?> loadedData;

    public ConstructableStorableLexicalAnalyser(StorageValue<?> data) {
        loadedData = data;
    }

    @Override
    public Token[] analyse(String sentence) {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }

    @Override
    public StorageValue<?> getStorageRepresentation() {
        return loadedData;
    }

}
