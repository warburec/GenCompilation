package component_construction.custom_components;

import code_generation.CodeGenerator;
import component_construction.factories.code_generation.CodeGeneratorFactory;
import component_construction.factories.lexical_analysis.LexicalAnalyserFactory;
import component_construction.factories.syntax_analysis.SyntaxAnalyserFactory;
import grammar_objects.Grammar;
import grammar_objects.GrammarParts;
import grammar_objects.RuleConvertor;
import lexical_analysis.DynamicTokenRegex;
import lexical_analysis.LexicalAnalyser;
import syntax_analysis.SyntaxAnalyser;

public class CustomCompilerFactory {
    
    public CustomCompiler produce(
        LexicalAnalyserFactory lexicalAnalyserFactory,
        SyntaxAnalyserFactory syntaxAnalyserFactory,
        CodeGeneratorFactory codeGeneratorFactory,
        Grammar grammar,
        String[] whitespaceDelimiters,
        String[] stronglyReservedWords,
        String[] weaklyReservedWords,
        DynamicTokenRegex[] dynamicTokenRegex,
        RuleConvertor ruleConvertor
    ) {
        if(whitespaceDelimiters == null) whitespaceDelimiters = new String[]{};
        if(stronglyReservedWords == null) stronglyReservedWords = new String[]{};
        if(weaklyReservedWords == null) weaklyReservedWords = new String[]{};
        if(dynamicTokenRegex == null) dynamicTokenRegex = new DynamicTokenRegex[]{};

        LexicalAnalyser lexicalAnalyser = lexicalAnalyserFactory.produceAnalyser(
            whitespaceDelimiters,
            stronglyReservedWords,
            weaklyReservedWords,
            dynamicTokenRegex
        );
        SyntaxAnalyser syntaxAnalyser = syntaxAnalyserFactory.produceAnalyser(grammar.getParts());
        CodeGenerator codeGenerator = codeGeneratorFactory.produceGenerator(ruleConvertor);

        return new CustomCompiler(
            lexicalAnalyser,
            syntaxAnalyser,
            codeGenerator
        );
    }
}
