package test_aids.storage_entities.custom_components;

import component_construction.storage.StorableCustomCompiler;
import test_aids.storage_entities.custom_components.loadable.*;

public class TestStorableCustomCompiler extends StorableCustomCompiler {

    public TestStorableCustomCompiler() {
        super(
            new LoadableStorableCustomLexicalAnalyser(), 
            new LoadableStorableCustomSyntaxAnalyser(), 
            new LoadableStorableCustomCodeGenerator()
        );
    }

}
