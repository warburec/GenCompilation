package component_construction.custom_components;

import code_generation.CodeGenerator;
import component_construction.factories.code_generation.CodeGeneratorFactory;
import component_construction.factories.lexical_analysis.LexicalAnalyserFactory;
import component_construction.factories.syntax_analysis.SyntaxAnalyserFactory;
import grammar_objects.Grammar;
import grammar_objects.RuleConvertor;
import lexical_analysis.*;
import syntax_analysis.SyntaxAnalyser;

/**
 * A factory for {@code CustomCompilers}
 */
public class CustomCompilerFactory {
    
    /**
     * Builds and produces a {@code CustomCompiler} with user-specified components
     * @param lexicalAnalyserFactory A factory for producing {@code LexicalAnalysers} for use within produced {@code CustomCompilers}
     * @param syntaxAnalyserFactory A factory for producing {@code SyntaxAnalysers} for use within produced {@code CustomCompilers}
     * @param codeGeneratorFactory A factory for producing {@code CodeGenerators} for use within produced {@code CustomCompilers}
     * @param grammar The grammar to be used by produced {@code CustomCompilers}
     * @param ruleConvertor The convertor to sanction grammar rules within produced {@code CustomCompilers}
     * @param whitespaceDelimiters An array of designated whitespace delimiter strings for compilation
     * @param stronglyReservedWords An array of designated reserved words for compilation. These words may not be used within other tokens
     * @param weaklyReservedWords An array of reserved words that may be used as part of other tokens
     * @param dynamicTokenRegex An array of {@code DynamicTokenRegex} for tokens whix may take many forms according to the provided regex
     * @return A produced {@code CustomCompiler}
     */
    public CustomCompiler produce(
        LexicalAnalyserFactory lexicalAnalyserFactory,
        SyntaxAnalyserFactory syntaxAnalyserFactory,
        CodeGeneratorFactory codeGeneratorFactory,
        Grammar grammar,
        RuleConvertor ruleConvertor,
        String[] whitespaceDelimiters,
        String[] stronglyReservedWords,
        String[] weaklyReservedWords,
        DynamicTokenRegex[] dynamicTokenRegex
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
