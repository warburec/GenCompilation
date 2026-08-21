package component_construction.storage.builders;

import code_generation.CodeGenerator;
import component_construction.builders.CompilerBuilderTemplate;
import component_construction.custom_components.*;
import component_construction.storage.StorableCustomCompiler;
import grammar_objects.GrammarParts;
import lexical_analysis.LexicalAnalyser;
import syntax_analysis.SyntaxAnalyser;

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
