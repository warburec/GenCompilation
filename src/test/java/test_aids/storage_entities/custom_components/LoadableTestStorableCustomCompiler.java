package test_aids.storage_entities.custom_components;

import java.util.Map;

import component_construction.storage.StorableCustomCompiler;
import storage.external_interfaces.Loadable;
import storage.storage_values.MapStorageValue;
import storage.storage_values.StorageValue;

public class LoadableTestStorableCustomCompiler extends StorableCustomCompiler implements Loadable {

    public LoadableTestStorableCustomCompiler() {
        super(
            new LoadableCustomLexicalAnalyser(),
            new LoadableCustomSyntaxAnalyser(),
            new LoadableCustomCodeGenerator()
        );
    }

    @Override
    public void load(StorageValue<?> data) {
        Map<String, StorageValue<?>> description = ((MapStorageValue)data).getValue();

        lexicalAnalyser = new LoadableCustomLexicalAnalyser();
        syntaxAnalyser = new LoadableCustomSyntaxAnalyser();
        codeGenerator = new LoadableCustomCodeGenerator();

        ((LoadableCustomLexicalAnalyser)lexicalAnalyser).load(description.get("lexicalAnalyser"));
        ((LoadableCustomSyntaxAnalyser)syntaxAnalyser).load(description.get("syntaxAnalyser"));
        ((LoadableCustomCodeGenerator)codeGenerator).load(description.get("codeGenerator"));
    }
    
}
