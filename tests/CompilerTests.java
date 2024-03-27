package tests;

import org.junit.Test;

import builders.CompilerBuilder;
import builders.Compiler;
import builders.concrete_factories.GeneralLexicalAnalyserFactory;
import grammar_objects.Identifier;
import grammars.basic_identifier.BasicIdentifierGrammar;
import grammars.basic_identifier.convertors.XToXToYSemantic;
import builders.concrete_factories.*;
import grammar_objects.Literal;
import lexical_analysis.DynamicTokenRegex;
import syntax_analysis.parsing.ParseFailedException;

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
            new String[] {}, 
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

        System.out.println(compiler.compile(inputSentence));
    }
}
