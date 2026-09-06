package test_aids.storage_entities.custom_components;

import component_construction.storage.StorableCustomCompiler;

public class TestStorableCustomCompiler extends StorableCustomCompiler {

    public TestStorableCustomCompiler() {
        super(
            new TestCustomLexicalAnalyser(), 
            new TestCustomSyntaxAnalyser(), 
            new TestCustomCodeGenerator()
        );
    }

}
