package test_aids.storage_entities.custom_components;

import grammar_objects.Token;
import storage.external_interfaces.Storable;
import storage.storage_values.*;
import syntax_analysis.SyntaxAnalyser;
import syntax_analysis.parsing.*;

public class TestStorableCustomSyntaxAnalyser implements SyntaxAnalyser, Storable {

    public String value = null;

    public TestStorableCustomSyntaxAnalyser() {}

    public TestStorableCustomSyntaxAnalyser(String value) {
        this.value = value;
    }

    @Override
    public ParseState analyse(Token[] inputTokens) throws ParseFailedException {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }

    @Override
    public StorageValue<?> getStorageRepresentation() {
        return new StringStorageValue("TestStorableCustomSyntaxAnalyser description");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TestStorableCustomSyntaxAnalyser)) return false;
		
        TestStorableCustomSyntaxAnalyser other = (TestStorableCustomSyntaxAnalyser)obj;
        if (this.value != other.value) return false;

		return true;
    }
    
}
