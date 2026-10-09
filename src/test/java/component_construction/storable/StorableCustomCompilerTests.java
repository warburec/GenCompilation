package component_construction.storable;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import org.junit.jupiter.api.Test;

import storage.external_interfaces.Storable;
import storage.storage_values.*;
import test_aids.storage_entities.custom_components.*;

public class StorableCustomCompilerTests {
    
    @Test 
    public void getStorageRepresentation() {
        TestStorableCustomLexicalAnalyser lexicalAnalyser = new TestStorableCustomLexicalAnalyser();
        TestStorableCustomSyntaxAnalyser syntaxAnalyser = new TestStorableCustomSyntaxAnalyser();
        TestStorableCustomCodeGenerator codeGenerator = new TestStorableCustomCodeGenerator();

        StorableCustomCompiler compiler = new StorableCustomCompiler(
            lexicalAnalyser,
            syntaxAnalyser,
            codeGenerator
        );


        MapStorageValue actualRepresentation = compiler.getStorageRepresentation();


        MapStorageValue expectedRepresentation = new MapStorageValue(Map.of(
            "lexicalAnalyser", new ListStorageValue(
                new StringStorageValue(lexicalAnalyser.getClass().getName()),
                ((Storable)lexicalAnalyser).getStorageRepresentation()
            ),
            "syntaxAnalyser", new ListStorageValue(
                new StringStorageValue(syntaxAnalyser.getClass().getName()),
                ((Storable)syntaxAnalyser).getStorageRepresentation()
            ),
            "codeGenerator", new ListStorageValue(
                new StringStorageValue(codeGenerator.getClass().getName()),
                ((Storable)codeGenerator).getStorageRepresentation()
            )
        ));

        assertEquals(expectedRepresentation, actualRepresentation);
    }

    @Test
    public void equality() {
        TestStorableCustomLexicalAnalyser lexicalAnalyser = new TestStorableCustomLexicalAnalyser();
        TestStorableCustomSyntaxAnalyser syntaxAnalyser = new TestStorableCustomSyntaxAnalyser();
        TestStorableCustomCodeGenerator codeGenerator = new TestStorableCustomCodeGenerator();


        StorableCustomCompiler compiler1 = new StorableCustomCompiler(
            lexicalAnalyser,
            syntaxAnalyser,
            codeGenerator
        );
        StorableCustomCompiler compiler2 = new StorableCustomCompiler(
            lexicalAnalyser,
            syntaxAnalyser,
            codeGenerator
        );


        assertEquals(compiler1, compiler2);
    }

    @Test
    public void inequality_lexicalAnalyser() {
        TestStorableCustomLexicalAnalyser lexicalAnalyser1 = new TestStorableCustomLexicalAnalyser("1");
        TestStorableCustomLexicalAnalyser lexicalAnalyser2 = new TestStorableCustomLexicalAnalyser("2");
        TestStorableCustomSyntaxAnalyser syntaxAnalyser = new TestStorableCustomSyntaxAnalyser();
        TestStorableCustomCodeGenerator codeGenerator = new TestStorableCustomCodeGenerator();


        StorableCustomCompiler compiler1 = new StorableCustomCompiler(
            lexicalAnalyser1,
            syntaxAnalyser,
            codeGenerator
        );
        StorableCustomCompiler compiler2 = new StorableCustomCompiler(
            lexicalAnalyser2,
            syntaxAnalyser,
            codeGenerator
        );


        assertNotEquals(compiler1, compiler2);
    }

    @Test
    public void inequality_syntaxAnalyser() {
        TestStorableCustomLexicalAnalyser lexicalAnalyser = new TestStorableCustomLexicalAnalyser();
        TestStorableCustomSyntaxAnalyser syntaxAnalyser1 = new TestStorableCustomSyntaxAnalyser("1");
        TestStorableCustomSyntaxAnalyser syntaxAnalyser2 = new TestStorableCustomSyntaxAnalyser("2");
        TestStorableCustomCodeGenerator codeGenerator = new TestStorableCustomCodeGenerator();


        StorableCustomCompiler compiler1 = new StorableCustomCompiler(
            lexicalAnalyser,
            syntaxAnalyser1,
            codeGenerator
        );
        StorableCustomCompiler compiler2 = new StorableCustomCompiler(
            lexicalAnalyser,
            syntaxAnalyser2,
            codeGenerator
        );


        assertNotEquals(compiler1, compiler2);
    }

    @Test
    public void inequality_codeGenerator() {
        TestStorableCustomLexicalAnalyser lexicalAnalyser = new TestStorableCustomLexicalAnalyser();
        TestStorableCustomSyntaxAnalyser syntaxAnalyser = new TestStorableCustomSyntaxAnalyser();
        TestStorableCustomCodeGenerator codeGenerator1 = new TestStorableCustomCodeGenerator("1");
        TestStorableCustomCodeGenerator codeGenerator2 = new TestStorableCustomCodeGenerator("2");


        StorableCustomCompiler compiler1 = new StorableCustomCompiler(
            lexicalAnalyser,
            syntaxAnalyser,
            codeGenerator1
        );
        StorableCustomCompiler compiler2 = new StorableCustomCompiler(
            lexicalAnalyser,
            syntaxAnalyser,
            codeGenerator2
        );


        assertNotEquals(compiler1, compiler2);
    }
}
