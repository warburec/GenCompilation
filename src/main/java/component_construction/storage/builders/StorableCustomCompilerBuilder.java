package component_construction.storage.builders;

import component_construction.builders.CompilerBuilderTemplate;
import component_construction.storage.StorableCustomCompiler;
import grammar_objects.GrammarParts;

public class StorableCustomCompilerBuilder extends CompilerBuilderTemplate<StorableCustomCompilerBuilder, StorableCustomCompiler> {

    @Override
    protected StorableCustomCompilerBuilder getThis() {
        return this;
    }

    @Override
    public StorableCustomCompiler createCompiler() {
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
