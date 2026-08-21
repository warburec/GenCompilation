package component_construction.storage;

import java.util.Map;

import code_generation.CodeGenerator;
import component_construction.custom_components.*;
import component_construction.storage.dynamic_loading.LoadableBy;
import component_construction.storage.factories.StorableCustomCompilerFactory;
import lexical_analysis.LexicalAnalyser;
import storage.external_interfaces.Storable;
import storage.storage_values.*;
import syntax_analysis.SyntaxAnalyser;

public class StorableCustomCompiler extends CustomCompiler implements Storable, LoadableBy<StorableCustomCompilerFactory> {

    public <
        L extends LexicalAnalyser & Storable, 
        S extends SyntaxAnalyser & Storable,  
        C extends CodeGenerator & Storable
    >
    StorableCustomCompiler(
        LexicalAnalyser lexicalAnalyser, 
        SyntaxAnalyser syntaxAnalyser,
        CodeGenerator codeGenerator
    ) {
        super(lexicalAnalyser, syntaxAnalyser, codeGenerator);
    }

    @Override
    public MapStorageValue getStorageRepresentation() {
        return new MapStorageValue(Map.of(
            "lexicalAnalyser", new ListStorageValue(
                new StringStorageValue(lexicalAnalyser.getClass().getName()),
                ((Storable)lexicalAnalyser).getStorageRepresentation()
            ),
            "syntaxAnalyser", new ListStorageValue(
                new StringStorageValue(syntaxAnalyser.getClass().getName()),
                ((Storable)syntaxAnalyser).getStorageRepresentation()
            ),
            "codeGenerator", new ListStorageValue(
                new StringStorageValue(codeGenerator.getClass().getName()),
                ((Storable)codeGenerator).getStorageRepresentation()
            )
        ));
    }

}
