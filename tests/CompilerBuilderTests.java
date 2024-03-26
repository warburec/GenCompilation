package tests;

import org.junit.Test;

import builders.CompilerBuilder;
import builders.concrete_factories.*;
import grammar_objects.Identifier;
import grammar_objects.Literal;
import grammars.basic_identifier.BasicIdentifierGrammar;
import grammars.basic_identifier.convertors.XToXToYSemantic;
import lexical_analysis.DynamicTokenRegex;
import builders.Compiler;

public class CompilerBuilderTests {
    
    @Test
    public void basicCompilerBuilder() {
        CompilerBuilder builder = new CompilerBuilder();

        builder.setComponents(
            new GeneralLexicalAnalyserFactory(),
            new CLR1ParserFactory(), 
            new BasicCodeGenFactory(), 
            new BasicIdentifierGrammar(), 
            new XToXToYSemantic(), 
            new String[] {" "}, 
            new String[] {"+", "="}, 
            new String[] {}, 
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Z]+", Identifier.class, "identifier"),
                new DynamicTokenRegex("[1-9]+(.[0-9]+)?", Literal.class, "number")
            }
        );

        Compiler compiler = builder.createCompiler();
    }
}
