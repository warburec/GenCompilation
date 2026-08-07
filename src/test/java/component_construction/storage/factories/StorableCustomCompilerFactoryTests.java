package component_construction.storage.factories;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

import component_construction.custom_components.*;
import storage.storage_values.*;
import test_aids.storage_entities.custom_components.*;

public class StorableCustomCompilerFactoryTests {
    
    protected record TypedComponent<T>(String name, ListStorageValue description, Factory<T> factory) {}

    protected static final TypedComponent<CustomLexicalAnalyser> lexicalComponent = new TypedComponent<>(
        "TestCustomLexicalAnalyser",
        new ListStorageValue(
            new StringStorageValue("TestCustomLexicalAnalyser")
        ), 
        (description) -> {
            assertEquals(
                new ListStorageValue(
                    new StringStorageValue("TestCustomLexicalAnalyser")
                ), 
                description
            );

            return new TestCustomLexicalAnalyser();
        }
    );

    protected static final TypedComponent<CustomSyntaxAnalyser> syntaxComponent = new TypedComponent<>(
        "TestCustomSyntaxAnalyser",
        new ListStorageValue(
            new StringStorageValue("TestCustomSyntaxAnalyser")
        ),
        (description) -> {
            assertEquals(
                new ListStorageValue(
                    new StringStorageValue("TestCustomSyntaxAnalyser")
                ),
                description
            );

            return new TestCustomSyntaxAnalyser();
        }
    );

    protected static final TypedComponent<CustomCodeGenerator> codeGeneratorComponent = new TypedComponent<>(
        "TestCustomCodeGenerator",
        new ListStorageValue(
            new StringStorageValue("TestCustomCodeGenerator")
        ), 
        (description) -> {
            assertEquals(
                new ListStorageValue(
                    new StringStorageValue("TestCustomCodeGenerator")
                ), 
                description
            );

            return new TestCustomCodeGenerator();
        }
    );

    @Test
    public void produce() {
        StorableCustomCompilerFactory factory = new StorableCustomCompilerFactory()
            .addLexicalAnalyserFactory(
                lexicalComponent.name(),
                lexicalComponent.factory()
            )
            .addSyntaxAnalyserFactory(
                syntaxComponent.name(),
                syntaxComponent.factory()
            )
            .addCodeGeneratorFactory(
                codeGeneratorComponent.name(),
                codeGeneratorComponent.factory()
            );

        assertDoesNotThrow(() -> factory.produce(
            new MapStorageValue(Map.of(
                "lexicalAnalyser", new ListStorageValue(
                    new StringStorageValue("TestCustomLexicalAnalyser"),
                    lexicalComponent.description()
                ),
                "syntaxAnalyser", new ListStorageValue(
                    new StringStorageValue("TestCustomSyntaxAnalyser"),
                    syntaxComponent.description()
                ),
                "codeGenerator", new ListStorageValue(
                    new StringStorageValue("TestCustomCodeGenerator"),
                    codeGeneratorComponent.description()
                )
            ))
        ));
    }

    //In repositories
    //LoadableBy components
    //Loadable components
    //Default/Empty constructor without interfaces
    //Incorrect StorageValue type (i.e. not Map in produce())
}
