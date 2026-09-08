package test_aids.storage_entities.custom_components.loadable;

import component_construction.storage.dynamic_loading.ReflectivelyLoadable;
import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import storage.external_interfaces.Storable;
import storage.storage_values.*;

public class LoadableStorableCustomLexicalAnalyser implements LexicalAnalyser, ReflectivelyLoadable, Storable {
    
    public StorageValue<?> loadedData = new NullStorageValue();

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
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LoadableStorableCustomLexicalAnalyser)) return false;
        
		LoadableStorableCustomLexicalAnalyser other = (LoadableStorableCustomLexicalAnalyser)obj;
		return this.loadedData.equals(other.loadedData);
    }

}
