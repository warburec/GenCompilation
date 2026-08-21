package test_aids.storage_entities.custom_components;

import component_construction.custom_components.CustomLexicalAnalyser;
import grammar_objects.Token;
import storage.external_interfaces.Loadable;
import storage.storage_values.StorageValue;

public class LoadableCustomLexicalAnalyser implements CustomLexicalAnalyser, Loadable {

    @Override
    public Token[] analyse(String sentence) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }

    @Override
    public StorageValue<?> getStorageRepresentation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStorageRepresentation'");
    }
    
     @Override
    public void load(StorageValue<?> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'load'");
    }
    
}
