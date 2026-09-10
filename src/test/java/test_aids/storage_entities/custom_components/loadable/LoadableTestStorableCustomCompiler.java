package test_aids.storage_entities.custom_components.loadable;

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
        
        StorageValue<?> lexicalAnalyserDescription = ((ListStorageValue)description.get("lexicalAnalyser")).getValue().get(1);
        StorageValue<?> syntaxAnalyserDescription = ((ListStorageValue)description.get("lexicalAnalyser")).getValue().get(1);
        StorageValue<?> codeGeneratorDescription = ((ListStorageValue)description.get("lexicalAnalyser")).getValue().get(1);
        
        lexicalAnalyser = new LoadableStorableCustomLexicalAnalyser();
        syntaxAnalyser = new LoadableStorableCustomSyntaxAnalyser();
        codeGenerator = new LoadableStorableCustomCodeGenerator();

        ((LoadableStorableCustomLexicalAnalyser)lexicalAnalyser).load(lexicalAnalyserDescription);
        ((LoadableStorableCustomSyntaxAnalyser)syntaxAnalyser).load(syntaxAnalyserDescription);
        ((LoadableStorableCustomCodeGenerator)codeGenerator).load(codeGeneratorDescription);
    }
    
}
