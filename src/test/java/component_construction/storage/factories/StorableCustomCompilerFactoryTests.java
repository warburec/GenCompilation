package component_construction.storage.factories;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

import component_construction.custom_components.*;
import storage.storage_values.*;
import test_aids.storage_entities.custom_components.*;
import test_aids.storage_entities.custom_components.loadableBy.LoadableByCustomLexicalAnalyser;

public class StorableCustomCompilerFactoryTests {
    
    protected record TypedComponent<T>(String name, ListStorageValue description, Factory<T> factory) {}

    protected static final TypedComponent<CustomLexicalAnalyser> lexicalComponent = new TypedComponent<>(
        "TestLexicalAnalyser",
        new ListStorageValue(
            new StringStorageValue("TestLexicalAnalyser")
        ), 
        (description) -> {
            assertEquals(
                new ListStorageValue(
                    new StringStorageValue("TestLexicalAnalyser")
                ), 
                description
            );

            return new TestCustomLexicalAnalyser();
        }
    );

    protected static final TypedComponent<CustomSyntaxAnalyser> syntaxComponent = new TypedComponent<>(
        "TestSyntaxAnalyser",
        new ListStorageValue(
            new StringStorageValue("TestSyntaxAnalyser")
        ),
        (description) -> {
            assertEquals(
                new ListStorageValue(
                    new StringStorageValue("TestSyntaxAnalyser")
                ),
                description
            );

            return new TestCustomSyntaxAnalyser();
        }
    );

    protected static final TypedComponent<CustomCodeGenerator> codeGeneratorComponent = new TypedComponent<>(
        "TestCodeGenerator",
        new ListStorageValue(
            new StringStorageValue("TestCodeGenerator")
        ), 
        (description) -> {
            assertEquals(
                new ListStorageValue(
                    new StringStorageValue("TestCodeGenerator")
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
                    new StringStorageValue(lexicalComponent.name()),
                    lexicalComponent.description()
                ),
                "syntaxAnalyser", new ListStorageValue(
                    new StringStorageValue(syntaxComponent.name()),
                    syntaxComponent.description()
                ),
                "codeGenerator", new ListStorageValue(
                    new StringStorageValue(codeGeneratorComponent.name()),
                    codeGeneratorComponent.description()
                )
            ))
        ));
    }

    @Test
    public void produce_existingNamedClassComponents() {
        StorableCustomCompilerFactory factory = new StorableCustomCompilerFactory()
            .addLexicalAnalyserFactory(
                "TestCustomLexicalAnalyser",
                lexicalComponent.factory()
            )
            .addSyntaxAnalyserFactory(
                "TestCustomSyntaxAnalyser",
                syntaxComponent.factory()
            )
            .addCodeGeneratorFactory(
                "TestCustomCodeGenerator",
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

    @Test
    public void produce_LoadableByLexicalAnalyser() {
        StorableCustomCompilerFactory factory = new StorableCustomCompilerFactory()
            .addSyntaxAnalyserFactory(
                syntaxComponent.name(),
                syntaxComponent.factory()
            )
            .addCodeGeneratorFactory(
                codeGeneratorComponent.name(),
                codeGeneratorComponent.factory()
            );

        factory.produce(
            new MapStorageValue(Map.of(
                "lexicalAnalyser", new ListStorageValue(
                    new StringStorageValue(LoadableByCustomLexicalAnalyser.class.getName()),
                    lexicalComponent.description()
                ),
                "syntaxAnalyser", new ListStorageValue(
                    new StringStorageValue(syntaxComponent.name()),
                    syntaxComponent.description()
                ),
                "codeGenerator", new ListStorageValue(
                    new StringStorageValue(codeGeneratorComponent.name()),
                    codeGeneratorComponent.description()
                )
            ))
        );
    }
    
    //Loadable components
    //Default/Empty constructor without interfaces
    //Incorrect StorageValue type (i.e. not Map in produce())
}
