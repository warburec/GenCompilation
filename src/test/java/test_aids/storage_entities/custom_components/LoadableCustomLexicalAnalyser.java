package test_aids.storage_entities.custom_components;

import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import storage.external_interfaces.Loadable;
import storage.storage_values.StorageValue;

public class LoadableCustomLexicalAnalyser implements LexicalAnalyser, Loadable {

    @Override
    public Token[] analyse(String sentence) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }
    
     @Override
    public void load(StorageValue<?> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'load'");
    }
    
}
