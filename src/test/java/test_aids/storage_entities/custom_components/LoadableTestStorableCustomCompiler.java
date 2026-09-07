package test_aids.storage_entities.custom_components;

import java.util.Map;

import component_construction.storage.StorableCustomCompiler;
import storage.external_interfaces.Loadable;
import storage.storage_values.*;

public class LoadableTestStorableCustomCompiler extends StorableCustomCompiler implements Loadable {

    public LoadableTestStorableCustomCompiler() {
        super(
            new LoadableStorableCustomLexicalAnalyser(),
            new LoadableStorableCustomSyntaxAnalyser(),
            new LoadableStorableCustomCodeGenerator()
        );
    }

    @Override
    public void load(StorageValue<?> data) {
        Map<String, StorageValue<?>> description = ((MapStorageValue)data).getValue();

        lexicalAnalyser = new LoadableStorableCustomLexicalAnalyser();
        syntaxAnalyser = new LoadableStorableCustomSyntaxAnalyser();
        codeGenerator = new LoadableStorableCustomCodeGenerator();

        ((LoadableStorableCustomLexicalAnalyser)lexicalAnalyser).load(description.get("lexicalAnalyser"));
        ((LoadableStorableCustomSyntaxAnalyser)syntaxAnalyser).load(description.get("syntaxAnalyser"));
        ((LoadableStorableCustomCodeGenerator)codeGenerator).load(description.get("codeGenerator"));
    }
    
}
