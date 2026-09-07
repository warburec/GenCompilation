package test_aids.storage_entities.custom_components;

import grammar_objects.Token;
import storage.external_interfaces.Storable;
import storage.storage_values.*;
import syntax_analysis.SyntaxAnalyser;
import syntax_analysis.parsing.*;

public class TestStorableCustomSyntaxAnalyser implements SyntaxAnalyser, Storable {

    @Override
    public ParseState analyse(Token[] inputTokens) throws ParseFailedException {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }

    @Override
    public StorageValue<?> getStorageRepresentation() {
        return new StringStorageValue("TestStorableCustomSyntaxAnalyser");
    }
    
}
