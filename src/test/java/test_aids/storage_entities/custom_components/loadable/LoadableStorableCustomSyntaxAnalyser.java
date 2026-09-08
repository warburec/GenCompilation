package test_aids.storage_entities.custom_components.loadable;

import component_construction.storage.dynamic_loading.ReflectivelyLoadable;
import grammar_objects.Token;
import storage.external_interfaces.*;
import storage.storage_values.*;
import syntax_analysis.SyntaxAnalyser;
import syntax_analysis.parsing.*;

public class LoadableStorableCustomSyntaxAnalyser implements SyntaxAnalyser, ReflectivelyLoadable, Storable {

    public StorageValue<?> loadedData = new NullStorageValue();

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
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LoadableStorableCustomSyntaxAnalyser)) return false;
        
		LoadableStorableCustomSyntaxAnalyser other = (LoadableStorableCustomSyntaxAnalyser)obj;
		return this.loadedData.equals(other.loadedData);
    }

}
