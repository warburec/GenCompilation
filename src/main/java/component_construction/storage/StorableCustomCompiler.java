package component_construction.storage;

import java.util.Map;

import code_generation.CodeGenerator;
import component_construction.custom_components.*;
import component_construction.storage.dynamic_loading.LoadableBy;
import component_construction.storage.exceptions.NonStorableComponentException;
import component_construction.storage.factories.StorableCustomCompilerFactory;
import lexical_analysis.LexicalAnalyser;
import storage.external_interfaces.Storable;
import storage.storage_values.*;
import syntax_analysis.SyntaxAnalyser;

public class StorableCustomCompiler extends CustomCompiler implements Storable, LoadableBy<StorableCustomCompilerFactory> {

    /**
     * Creates a new StorableCustomCompiler. All components must implement Storable.
     * @param lexicalAnalyser A Storable LexicalAnalyser for this compiler
     * @param syntaxAnalyser A Storable SyntaxAnalyser for this compiler
     * @param codeGenerator A Storable CodeGenerator for this compiler
     * @throws NonStorableComponentException A component was introduced without implementing Storable
     */
    public StorableCustomCompiler(
        LexicalAnalyser lexicalAnalyser, 
        SyntaxAnalyser syntaxAnalyser,
        CodeGenerator codeGenerator
    ) throws NonStorableComponentException {
        super(lexicalAnalyser, syntaxAnalyser, codeGenerator);
        
        if (!(lexicalAnalyser instanceof Storable)) throw new NonStorableComponentException(lexicalAnalyser);
        if (!(syntaxAnalyser instanceof Storable)) throw new NonStorableComponentException(syntaxAnalyser);
        if (!(codeGenerator instanceof Storable)) throw new NonStorableComponentException(codeGenerator);
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StorableCustomCompiler)) return false;
        return super.equals(obj);
    }

}
