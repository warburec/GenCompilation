package test_aids.storage_entities.custom_components.loadable;

import code_generation.CodeGenerator;
import component_construction.storage.StorableCustomCompiler;
import component_construction.storage.dynamic_loading.ReflectivelyLoadable;
import storage.external_interfaces.*;
import storage.storage_values.*;
import syntax_analysis.parsing.ParseState;

public class LoadableStorableCustomCodeGenerator implements CodeGenerator, ReflectivelyLoadable, Storable {

    public StorageValue<?> loadedData = new NullStorageValue();

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

	@Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LoadableStorableCustomCodeGenerator)) return false;
        
		LoadableStorableCustomCodeGenerator other = (LoadableStorableCustomCodeGenerator)obj;
		return this.loadedData.equals(other.loadedData);
    }

}
