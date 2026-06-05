package component_construction.storage;

import code_generation.CodeGenerator;
import component_construction.builders.CompilerBuilderTemplate;
import component_construction.custom_components.*;
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

        LexicalAnalyser lexicalAnalyser = lexicalAnalyserFactory.produceAnalyser(
            whitespaceDelimiters,
            stronglyReservedWords,
            weaklyReservedWords,
            dynamicTokenRegex
        );
        SyntaxAnalyser syntaxAnalyser = syntaxAnalyserFactory.produceAnalyser(parts);
        CodeGenerator codeGenerator = codeGeneratorFactory.produceGenerator(ruleConvertor);

        if(!(lexicalAnalyser instanceof CustomLexicalAnalyser)) throw new RuntimeException("TODO"); //TODO
        if(!(syntaxAnalyser instanceof CustomSyntaxAnalyser)) throw new RuntimeException("TODO"); //TODO
        if(!(codeGenerator instanceof CustomCodeGenerator)) throw new RuntimeException("TODO"); //TODO

        return new StorableCustomCompiler(
            (CustomLexicalAnalyser)lexicalAnalyser,
            (CustomSyntaxAnalyser)syntaxAnalyser,
            (CustomCodeGenerator)codeGenerator
        );
    }
}
