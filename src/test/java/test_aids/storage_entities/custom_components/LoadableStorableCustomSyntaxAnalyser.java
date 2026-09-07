package test_aids.storage_entities.custom_components;

import grammar_objects.Token;
import storage.external_interfaces.*;
import storage.storage_values.StorageValue;
import syntax_analysis.SyntaxAnalyser;
import syntax_analysis.parsing.*;

public class LoadableStorableCustomSyntaxAnalyser implements SyntaxAnalyser, Loadable, Storable {

    public StorageValue<?> loadedData;

    @Override
    public ParseState analyse(Token[] inputTokens) throws ParseFailedException {
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
    
}
