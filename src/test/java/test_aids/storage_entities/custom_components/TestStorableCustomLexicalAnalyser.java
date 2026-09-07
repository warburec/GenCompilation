package test_aids.storage_entities.custom_components;

import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import storage.external_interfaces.Storable;
import storage.storage_values.*;

public class TestStorableCustomLexicalAnalyser implements LexicalAnalyser, Storable {

    @Override
    public Token[] analyse(String sentence) {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }

    @Override
    public StorageValue<?> getStorageRepresentation() {
        return new StringStorageValue("TestStorableCustomLexicalAnalyser");
    }
    
}
