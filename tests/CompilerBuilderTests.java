package tests;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import builders.CompilerBuilder;
import builders.ParameterError;
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


        builder.createCompiler();
    }

    @Test
    public void nullComponentInputs() {
        CompilerBuilder builder1 = new CompilerBuilder();
        CompilerBuilder builder2 = new CompilerBuilder();
        CompilerBuilder builder3 = new CompilerBuilder();
        CompilerBuilder builder4 = new CompilerBuilder();
        CompilerBuilder builder5 = new CompilerBuilder();

        builder1.setComponents(
            null,
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
        builder2.setComponents(
            new GeneralLexicalAnalyserFactory(),
            null, 
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
        builder3.setComponents(
            new GeneralLexicalAnalyserFactory(),
            new CLR1ParserFactory(), 
            null, 
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
        builder4.setComponents(
            new GeneralLexicalAnalyserFactory(),
            new CLR1ParserFactory(), 
            new BasicCodeGenFactory(), 
            null, 
            new XToXToYSemantic(), 
            new String[] {" "}, 
            new String[] {"+", "="}, 
            new String[] {}, 
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Z]+", Identifier.class, "identifier"),
                new DynamicTokenRegex("[1-9]+(.[0-9]+)?", Literal.class, "number")
            }
        );
        builder5.setComponents(
            new GeneralLexicalAnalyserFactory(),
            new CLR1ParserFactory(), 
            new BasicCodeGenFactory(), 
            new BasicIdentifierGrammar(), 
            null, 
            new String[] {" "}, 
            new String[] {"+", "="}, 
            new String[] {}, 
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Z]+", Identifier.class, "identifier"),
                new DynamicTokenRegex("[1-9]+(.[0-9]+)?", Literal.class, "number")
            }
        );


        assertThrows(ParameterError.class, () -> builder1.createCompiler());
        assertThrows(ParameterError.class, () -> builder2.createCompiler());
        assertThrows(ParameterError.class, () -> builder3.createCompiler());
        assertThrows(ParameterError.class, () -> builder4.createCompiler());
        assertThrows(ParameterError.class, () -> builder5.createCompiler());
    }
}
