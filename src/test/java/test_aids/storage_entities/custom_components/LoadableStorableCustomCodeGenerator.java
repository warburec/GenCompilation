package test_aids.storage_entities.custom_components;

import code_generation.CodeGenerator;
import storage.external_interfaces.*;
import storage.storage_values.StorageValue;
import syntax_analysis.parsing.ParseState;

public class LoadableStorableCustomCodeGenerator implements CodeGenerator, Loadable, Storable {

    public StorageValue<?> loadedData;

	@Override
	public String generate(ParseState parseRoot) {
		throw new UnsupportedOperationException("Unimplemented method 'generate'");
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
