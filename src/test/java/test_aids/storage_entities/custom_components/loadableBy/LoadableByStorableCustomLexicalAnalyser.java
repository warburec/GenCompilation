package test_aids.storage_entities.custom_components.loadableBy;

import component_construction.storage.dynamic_loading.LoadableBy;
import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import storage.external_interfaces.Storable;
import storage.storage_values.*;
import test_aids.storage_entities.custom_components.loadable.LoadableStorableCustomSyntaxAnalyser;

public class LoadableByStorableCustomLexicalAnalyser implements LexicalAnalyser, LoadableBy<LoadableByCustomLexicalAnalyserLoader>, Storable {

    public StorageValue<?> loadedData = new NullStorageValue();

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
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LoadableByStorableCustomLexicalAnalyser)) return false;
        
		LoadableByStorableCustomLexicalAnalyser other = (LoadableByStorableCustomLexicalAnalyser)obj;
		return this.loadedData.equals(other.loadedData);
    }
    
}
