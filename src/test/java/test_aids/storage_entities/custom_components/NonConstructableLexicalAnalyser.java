package test_aids.storage_entities.custom_components;

import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import storage.storage_values.*;

public class NonConstructableLexicalAnalyser implements LexicalAnalyser {
    
    public StorageValue<?> loadedData = new NullStorageValue();

    public NonConstructableLexicalAnalyser() {}

    public NonConstructableLexicalAnalyser(int a, String b) {}

    @Override
    public Token[] analyse(String sentence) {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NonConstructableLexicalAnalyser)) return false;
        
		NonConstructableLexicalAnalyser other = (NonConstructableLexicalAnalyser)obj;
		return this.loadedData.equals(other.loadedData);
    }

}
