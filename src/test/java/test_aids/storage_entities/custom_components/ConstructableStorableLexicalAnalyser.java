package test_aids.storage_entities.custom_components;

import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import storage.external_interfaces.Storable;
import storage.storage_values.*;

public class ConstructableStorableLexicalAnalyser implements LexicalAnalyser, Storable {
    
    public StorageValue<?> loadedData = new NullStorageValue();

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConstructableStorableLexicalAnalyser)) return false;
        
		ConstructableStorableLexicalAnalyser other = (ConstructableStorableLexicalAnalyser)obj;
		return this.loadedData.equals(other.loadedData);
    }

}
