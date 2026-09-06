package test_aids.storage_entities.custom_components;

import code_generation.CodeGenerator;
import storage.external_interfaces.Loadable;
import storage.storage_values.StorageValue;
import syntax_analysis.parsing.ParseState;

public class LoadableCustomCodeGenerator implements CodeGenerator, Loadable {

    public StorageValue<?> loadedData;

	@Override
	public String generate(ParseState parseRoot) {
		throw new UnsupportedOperationException("Unimplemented method 'generate'");
	}
    
    @Override
	public void load(StorageValue<?> data) {
        loadedData = data;
    }

}
