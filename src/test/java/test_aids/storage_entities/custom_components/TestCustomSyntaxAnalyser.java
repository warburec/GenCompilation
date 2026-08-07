package test_aids.storage_entities.custom_components;

import component_construction.custom_components.CustomSyntaxAnalyser;
import grammar_objects.Token;
import storage.storage_values.StorageValue;
import syntax_analysis.parsing.*;

public class TestCustomSyntaxAnalyser implements CustomSyntaxAnalyser {

    @Override
    public ParseState analyse(Token[] inputTokens) throws ParseFailedException {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }

    @Override
    public StorageValue<?> getStorageRepresentation() {
        throw new UnsupportedOperationException("Unimplemented method 'getStorageRepresentation'");
    }

    @Override
    public void load(StorageValue<?> data) {
        throw new UnsupportedOperationException("Unimplemented method 'load'");
    }
    
}
