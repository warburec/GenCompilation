package test_aids.storage_entities.custom_components.loadableBy;

import component_construction.storage.dynamic_loading.Loader;
import storage.storage_values.StorageValue;

public class LoadableByCustomLexicalAnalyserLoader implements Loader<LoadableByCustomLexicalAnalyser> {

    @Override
    public LoadableByCustomLexicalAnalyser produce(StorageValue<?> loadValue) {
        LoadableByCustomLexicalAnalyser lex = new LoadableByCustomLexicalAnalyser();
        lex.loadInternals(loadValue);
        
        return lex;
    }
    
}
