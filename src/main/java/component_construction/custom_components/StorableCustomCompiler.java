package component_construction.custom_components;

import java.util.Map;

import storage.external_interfaces.StorableAndLoadable;
import storage.storage_values.MapStorageValue;
import storage.storage_values.StorageValue;

public class StorableCustomCompiler extends CustomCompiler implements StorableAndLoadable {

    public StorableCustomCompiler(
        CustomLexicalAnalyser lexicalAnalyser, 
        CustomSyntaxAnalyser syntaxAnalyser,
        CustomCodeGenerator codeGenerator
    ) {
        super(lexicalAnalyser, syntaxAnalyser, codeGenerator);
    }
    

    @Override
    public MapStorageValue getStorageRepresentation() {
        return new MapStorageValue(Map.of(
            "lexicalAnalyser", ((CustomLexicalAnalyser)lexicalAnalyser).getStorageRepresentation(),
            "syntaxAnalyser", ((CustomSyntaxAnalyser)syntaxAnalyser).getStorageRepresentation(),
            "codeGenerator", ((CustomCodeGenerator)codeGenerator).getStorageRepresentation()
        ));
    }

    @Override
    public void load(StorageValue<?> data) {
        MapStorageValue map = (MapStorageValue) data;
        Map<String, StorageValue<?>> value = map.getValue();

        lexicalAnalyser = new CustomLexicalAnalyser();
        syntaxAnalyser = new CustomSyntaxAnalyser();
        codeGenerator = new CustomCodeGenerator();

        ((CustomLexicalAnalyser)lexicalAnalyser).load(value.get("lexicalAnalyser"));
        ((CustomSyntaxAnalyser)syntaxAnalyser).load(value.get("syntaxAnalyser"));
        ((CustomCodeGenerator)codeGenerator).load(value.get("codeGenerator"));
    }
}
