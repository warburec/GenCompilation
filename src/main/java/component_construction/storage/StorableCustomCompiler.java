package component_construction.storage;

import java.util.Map;
import component_construction.custom_components.*;
import component_construction.storage.dynamic_loading.LoadableBy;
import component_construction.storage.factories.StorableCustomCompilerFactory;
import storage.external_interfaces.Storable;
import storage.storage_values.*;

public class StorableCustomCompiler extends CustomCompiler implements Storable, LoadableBy<StorableCustomCompilerFactory> {

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
            "lexicalAnalyser", new ListStorageValue(
                new StringStorageValue(lexicalAnalyser.getClass().getName()),
                ((CustomLexicalAnalyser)lexicalAnalyser).getStorageRepresentation()
            ),
            "syntaxAnalyser", new ListStorageValue(
                new StringStorageValue(syntaxAnalyser.getClass().getName()),
                ((CustomSyntaxAnalyser)syntaxAnalyser).getStorageRepresentation()
            ),
            "codeGenerator", new ListStorageValue(
                new StringStorageValue(codeGenerator.getClass().getName()),
                ((CustomCodeGenerator)codeGenerator).getStorageRepresentation()
            )
        ));
    }

}
