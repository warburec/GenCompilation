package component_construction.storable.builders;

import component_construction.builders.CompilerBuilderTemplate;
import component_construction.storable.StorableCustomCompiler;
import component_construction.storable.exceptions.NonStorableComponentException;
import grammar_objects.GrammarParts;

public class StorableCustomCompilerBuilder extends CompilerBuilderTemplate<StorableCustomCompilerBuilder, StorableCustomCompiler> {

    @Override
    protected StorableCustomCompilerBuilder getThis() {
        return this;
    }

    @Override
    public StorableCustomCompiler createCompiler() throws NonStorableComponentException {
        checkForCompleteBuild();

        GrammarParts parts = grammar.getParts();

        return new StorableCustomCompiler(
            lexicalAnalyserFactory.produceAnalyser(
                whitespaceDelimiters,
                stronglyReservedWords,
                weaklyReservedWords,
                dynamicTokenRegex
            ),
            syntaxAnalyserFactory.produceAnalyser(parts),
            codeGeneratorFactory.produceGenerator(ruleConvertor)
        );
    }
}
