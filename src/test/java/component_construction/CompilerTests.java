package component_construction;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import component_construction.builders.CompilerBuilder;
import component_construction.factories.code_generation.BasicCodeGenFactory;
import component_construction.factories.lexical_analysis.GeneralLexicalAnalyserFactory;
import component_construction.factories.syntax_analysis.CLR1ParserFactory;
import grammars.basic_identifier.BasicIdentifierGrammar;
import grammars.basic_identifier.convertors.XToYToXSemantic;
import lexical_analysis.DynamicTokenRegex;
import syntax_analysis.parsing.ParseFailedException;
import test_aids.GrammarType;
import test_aids.test_grammars.basic_identifier.BasicIdentifierTestGrammar;

public class CompilerTests {
    
    @Test
    public void basicIdentifierCompiler() throws ParseFailedException {
        CompilerBuilder builder = new CompilerBuilder();

        builder.setComponents(
            new GeneralLexicalAnalyserFactory(),
            new CLR1ParserFactory(), 
            new BasicCodeGenFactory(), 
            BasicIdentifierGrammar.produce(), 
            XToYToXSemantic.produce(), 
            new String[] {" ", "\n", "\r", "\t"}, 
            new String[] {"+", "=", ";"},
            new String[] {},
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Za-z]+", "identifier"),
                new DynamicTokenRegex("[0-9]+|[0-9]+.[0-9]+", "number") //TODO: Using [0-9]+(\\.[0-9]+)? would be better
            }
        );

        Compiler compiler = builder.createCompiler();

        String inputSentence = """
            x = 1 + 2;
            y = x + 3;
            x = y + 0;
            """;


        String output = compiler.compile(inputSentence);


        String expected = new BasicIdentifierTestGrammar().getGrammar(GrammarType.LR0).getCodeGeneration("Java XToYToXSemantic");
        assertEquals(expected, output);
    }
}
