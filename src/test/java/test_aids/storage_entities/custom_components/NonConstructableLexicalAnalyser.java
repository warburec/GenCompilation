package test_aids.storage_entities.custom_components;

import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import storage.storage_values.StorageValue;

public class NonConstructableLexicalAnalyser implements LexicalAnalyser {
    
    public StorageValue<?> loadedData;

    public NonConstructableLexicalAnalyser() {}

    public NonConstructableLexicalAnalyser(int a, String b) {}

    @Override
    public Token[] analyse(String sentence) {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }

}
