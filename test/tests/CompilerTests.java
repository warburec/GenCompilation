package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import builders.CompilerBuilder;
import builders.Compiler;
import grammars.basic_identifier.BasicIdentifierGrammar;
import grammars.basic_identifier.convertors.XToYToXSemantic;
import builders.concrete_factories.*;
import lexical_analysis.DynamicTokenRegex;
import syntax_analysis.parsing.ParseFailedException;
import tests.test_aids.GrammarType;
import tests.test_aids.test_grammars.basic_identifier.BasicIdentifierTestGrammar;

public class CompilerTests {
    
    @Test
    public void basicIdentifierCompiler() throws ParseFailedException {
        CompilerBuilder builder = new CompilerBuilder();

        builder.setComponents(
            new GeneralLexicalAnalyserFactory(),
            new CLR1ParserFactory(), 
            new BasicCodeGenFactory(), 
            new BasicIdentifierGrammar(), 
            new XToYToXSemantic(), 
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


        String expected = new BasicIdentifierTestGrammar(GrammarType.LR0).getGeneratedCode("XToYToXSemantic", "Java");
        assertEquals(expected, output);
    }
}
