package test_aids.storage_entities.custom_components;

import grammar_objects.Token;
import storage.external_interfaces.Loadable;
import storage.storage_values.StorageValue;
import syntax_analysis.SyntaxAnalyser;
import syntax_analysis.parsing.ParseFailedException;
import syntax_analysis.parsing.ParseState;

public class LoadableCustomSyntaxAnalyser implements SyntaxAnalyser, Loadable {

    public StorageValue<?> loadedData;

    @Override
    public ParseState analyse(Token[] inputTokens) throws ParseFailedException {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }

    @Override
    public void load(StorageValue<?> data) {
        loadedData = data;
    }
    
}
