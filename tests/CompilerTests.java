package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import builders.CompilerBuilder;
import builders.Compiler;
import grammar_objects.Identifier;
import grammars.basic_identifier.BasicIdentifierGrammar;
import grammars.basic_identifier.convertors.XToXToYSemantic;
import builders.concrete_factories.*;
import grammar_objects.Literal;
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
            new XToXToYSemantic(), 
            new String[] {" ", "\n", "\r", "\t"}, 
            new String[] {"+", "=", ";"},
            new String[] {}, //TODO: Place reasonable troubleshooting description for failing parsing to check that weakly and strongly reserved words are defined correctly
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Za-z]+", Identifier.class, "identifier"),
                new DynamicTokenRegex("[0-9]+|[0-9]+.[0-9]+", Literal.class, "number") //TODO: Using [0-9]+(\\.[0-9]+)? would be better
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
