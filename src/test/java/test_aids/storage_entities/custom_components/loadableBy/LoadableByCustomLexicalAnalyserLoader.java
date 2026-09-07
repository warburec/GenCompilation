package test_aids.storage_entities.custom_components.loadableBy;

import component_construction.storage.dynamic_loading.Loader;
import storage.storage_values.StorageValue;

public class LoadableByCustomLexicalAnalyserLoader implements Loader<LoadableByStorableCustomLexicalAnalyser> {

    @Override
    public LoadableByStorableCustomLexicalAnalyser produce(StorageValue<?> loadValue) {
        LoadableByStorableCustomLexicalAnalyser lex = new LoadableByStorableCustomLexicalAnalyser();
        lex.loadInternals(loadValue);
        
        return lex;
    }
    
}
