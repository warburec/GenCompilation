package test_aids.storage_entities.custom_components;

import component_construction.storage.dynamic_loading.ReflectivelyLoadable;
import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import storage.storage_values.StorageValue;

public class LoadableCustomLexicalAnalyser implements LexicalAnalyser, ReflectivelyLoadable {
    
    public StorageValue<?> loadedData;

    @Override
    public Token[] analyse(String sentence) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }
    
     @Override
    public void load(StorageValue<?> data) {
        loadedData = data;
    }
    
}
