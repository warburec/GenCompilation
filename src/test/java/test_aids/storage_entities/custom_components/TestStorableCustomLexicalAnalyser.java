package test_aids.storage_entities.custom_components;

import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import storage.external_interfaces.Storable;
import storage.storage_values.*;

public class TestStorableCustomLexicalAnalyser implements LexicalAnalyser, Storable {

    public String value = null;

    public TestStorableCustomLexicalAnalyser() {}

    public TestStorableCustomLexicalAnalyser(String value) {
        this.value = value;
    }

    @Override
    public Token[] analyse(String sentence) {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }

    @Override
    public StorageValue<?> getStorageRepresentation() {
        return new StringStorageValue("TestStorableCustomLexicalAnalyser description");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TestStorableCustomLexicalAnalyser)) return false;

        TestStorableCustomLexicalAnalyser other = (TestStorableCustomLexicalAnalyser)obj;
        if (this.value != other.value) return false;

		return true;
    }
    
}
