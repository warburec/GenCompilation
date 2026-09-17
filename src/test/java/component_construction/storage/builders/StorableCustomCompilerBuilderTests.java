package component_construction.storage.builders;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import component_construction.ParameterError;
import component_construction.storage.exceptions.NonStorableComponentException;
import grammars.basic_identifier.BasicIdentifierGrammar;
import grammars.basic_identifier.convertors.XToYToXSemantic;
import lexical_analysis.DynamicTokenRegex;
import test_aids.storage_entities.custom_components.factories.*;

public class StorableCustomCompilerBuilderTests {

    @Test
    public void getSelf() {
        StorableCustomCompilerBuilder builder = new StorableCustomCompilerBuilder();


        assertEquals(builder, builder.getThis());
    }

    @Test
    public void basicCompilerBuilder() {
        StorableCustomCompilerBuilder builder = new StorableCustomCompilerBuilder();
        builder.setComponents(
            new TestStorableCustomLexicalAnalyserFactory(),
            new TestStorableCustomSyntaxAnalyserFactory(), 
            new TestStorableCustomCodeGeneratorFactory(), 
            BasicIdentifierGrammar.produce(), 
            XToYToXSemantic.produce(), 
            new String[] {" "}, 
            new String[] {"+", "=", ";"}, 
            new String[] {}, 
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Za-z]+", "identifier"),
                new DynamicTokenRegex("[0-9]+(\\.[0-9]+)?", "number") //TODO: Using [0-9]+(\\.[0-9]+)? for all tests would be better
            }
        );


        assertDoesNotThrow(() -> builder.createCompiler());
    }

    @Test
    public void nullComponentInputs() {
        StorableCustomCompilerBuilder builder1 = new StorableCustomCompilerBuilder();
        StorableCustomCompilerBuilder builder2 = new StorableCustomCompilerBuilder();
        StorableCustomCompilerBuilder builder3 = new StorableCustomCompilerBuilder();
        StorableCustomCompilerBuilder builder4 = new StorableCustomCompilerBuilder();
        StorableCustomCompilerBuilder builder5 = new StorableCustomCompilerBuilder();

        builder1.setComponents(
            null,
            new TestStorableCustomSyntaxAnalyserFactory(), 
            new TestStorableCustomCodeGeneratorFactory(), 
            BasicIdentifierGrammar.produce(), 
            XToYToXSemantic.produce(), 
            new String[] {" "}, 
            new String[] {"+", "=", ";"}, 
            new String[] {}, 
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Za-z]+", "identifier"),
                new DynamicTokenRegex("[0-9]+(\\.[0-9]+)?", "number")
            }
        );
        builder2.setComponents(
            new TestStorableCustomLexicalAnalyserFactory(),
            null, 
            new TestStorableCustomCodeGeneratorFactory(), 
            BasicIdentifierGrammar.produce(), 
            XToYToXSemantic.produce(), 
            new String[] {" "}, 
            new String[] {"+", "=", ";"}, 
            new String[] {}, 
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Za-z]+", "identifier"),
                new DynamicTokenRegex("[0-9]+(\\.[0-9]+)?", "number")
            }
        );
        builder3.setComponents(
            new TestStorableCustomLexicalAnalyserFactory(),
            new TestStorableCustomSyntaxAnalyserFactory(),
            null, 
            BasicIdentifierGrammar.produce(), 
            XToYToXSemantic.produce(), 
            new String[] {" "}, 
            new String[] {"+", "=", ";"}, 
            new String[] {}, 
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Za-z]+", "identifier"),
                new DynamicTokenRegex("[0-9]+(\\.[0-9]+)?", "number")
            }
        );
        builder4.setComponents(
            new TestStorableCustomLexicalAnalyserFactory(),
            new TestStorableCustomSyntaxAnalyserFactory(), 
            new TestStorableCustomCodeGeneratorFactory(), 
            null, 
            XToYToXSemantic.produce(), 
            new String[] {" "}, 
            new String[] {"+", "=", ";"}, 
            new String[] {}, 
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Za-z]+", "identifier"),
                new DynamicTokenRegex("[0-9]+(\\.[0-9]+)?", "number")
            }
        );
        builder5.setComponents(
            new TestStorableCustomLexicalAnalyserFactory(),
            new TestStorableCustomSyntaxAnalyserFactory(), 
            new TestStorableCustomCodeGeneratorFactory(), 
            BasicIdentifierGrammar.produce(), 
            null, 
            new String[] {" "}, 
            new String[] {"+", "=", ";"}, 
            new String[] {}, 
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Za-z]+", "identifier"),
                new DynamicTokenRegex("[0-9]+(\\.[0-9]+)?", "number")
            }
        );


        assertThrows(ParameterError.class, () -> builder1.createCompiler());
        assertThrows(ParameterError.class, () -> builder2.createCompiler());
        assertThrows(ParameterError.class, () -> builder3.createCompiler());
        assertThrows(ParameterError.class, () -> builder4.createCompiler());
        assertThrows(ParameterError.class, () -> builder5.createCompiler());
    }

    @Test
    public void nullLexicalInputs() {
        StorableCustomCompilerBuilder builder = new StorableCustomCompilerBuilder();
        builder.setComponents(
            new TestStorableCustomLexicalAnalyserFactory(),
            new TestStorableCustomSyntaxAnalyserFactory(), 
            new TestStorableCustomCodeGeneratorFactory(), 
            BasicIdentifierGrammar.produce(), 
            XToYToXSemantic.produce(),  
            null, 
            null,
            null, 
            null
        );


        assertDoesNotThrow(() -> builder.createCompiler());
    }

    @Test
    public void setcomponents_nonStorableLexicalAnalyser() {
        StorableCustomCompilerBuilder builder = new StorableCustomCompilerBuilder();
        builder.setComponents(
            new TestCustomLexicalAnalyserFactory(),
            new TestStorableCustomSyntaxAnalyserFactory(), 
            new TestStorableCustomCodeGeneratorFactory(), 
            BasicIdentifierGrammar.produce(), 
            XToYToXSemantic.produce(), 
            new String[] {" "}, 
            new String[] {"+", "=", ";"}, 
            new String[] {}, 
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Za-z]+", "identifier"),
                new DynamicTokenRegex("[0-9]+(\\.[0-9]+)?", "number")
            }
        );


        assertThrows(NonStorableComponentException.class, () -> builder.createCompiler());
    }

    @Test
    public void setcomponents_nonStorableSyntaxAnalyser() {
        StorableCustomCompilerBuilder builder = new StorableCustomCompilerBuilder();
        builder.setComponents(
            new TestStorableCustomLexicalAnalyserFactory(),
            new TestCustomSyntaxAnalyserFactory(), 
            new TestStorableCustomCodeGeneratorFactory(), 
            BasicIdentifierGrammar.produce(), 
            XToYToXSemantic.produce(), 
            new String[] {" "}, 
            new String[] {"+", "=", ";"}, 
            new String[] {}, 
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Za-z]+", "identifier"),
                new DynamicTokenRegex("[0-9]+(\\.[0-9]+)?", "number")
            }
        );


        assertThrows(NonStorableComponentException.class, () -> builder.createCompiler());
    }

    @Test
    public void setcomponents_nonStorableCodeGenerator() {
        StorableCustomCompilerBuilder builder = new StorableCustomCompilerBuilder();
        builder.setComponents(
            new TestStorableCustomLexicalAnalyserFactory(),
            new TestStorableCustomSyntaxAnalyserFactory(), 
            new TestCustomCodeGeneratorFactory(), 
            BasicIdentifierGrammar.produce(), 
            XToYToXSemantic.produce(), 
            new String[] {" "}, 
            new String[] {"+", "=", ";"}, 
            new String[] {}, 
            new DynamicTokenRegex[] {
                new DynamicTokenRegex("[A-Za-z]+", "identifier"),
                new DynamicTokenRegex("[0-9]+(\\.[0-9]+)?", "number")
            }
        );


        assertThrows(NonStorableComponentException.class, () -> builder.createCompiler());
    }

}
