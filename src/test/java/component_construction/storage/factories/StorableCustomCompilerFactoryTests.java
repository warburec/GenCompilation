package component_construction.storage.factories;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

import code_generation.CodeGenerator;
import component_construction.storage.exceptions.*;
import lexical_analysis.LexicalAnalyser;
import storage.storage_values.*;
import syntax_analysis.SyntaxAnalyser;
import test_aids.storage_entities.custom_components.*;
import test_aids.storage_entities.custom_components.loadableBy.LoadableByCustomLexicalAnalyser;

public class StorableCustomCompilerFactoryTests {
    
    protected record TypedComponent<T>(String name, ListStorageValue description, Factory<T> factory) {}

    protected static final TypedComponent<LexicalAnalyser> lexicalComponent = new TypedComponent<>(
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

    protected static final TypedComponent<SyntaxAnalyser> syntaxComponent = new TypedComponent<>(
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

    protected static final TypedComponent<CodeGenerator> codeGeneratorComponent = new TypedComponent<>(
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
    
    @Test
    public void produce_LoadableLexicalAnalyser() {
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
                    new StringStorageValue(LoadableCustomLexicalAnalyser.class.getName()),
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

    @Test
    public void produce_ConstructableLexicalAnalyser() {
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
                    new StringStorageValue(ConstructableLexicalAnalyser.class.getName()),
                    new ListStorageValue(List.of())
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

    @Test
    public void produce_incorrectFormat_noMap() {
        StorableCustomCompilerFactory factory = new StorableCustomCompilerFactory()
            .addSyntaxAnalyserFactory(
                syntaxComponent.name(),
                syntaxComponent.factory()
            )
            .addCodeGeneratorFactory(
                codeGeneratorComponent.name(),
                codeGeneratorComponent.factory()
            );

        assertThrows(IncorrectLoadValueFormat.class, () -> factory.produce(
            new ListStorageValue(List.of(
                new ListStorageValue(
                    new StringStorageValue("lexicalAnalyser"), 
                    new ListStorageValue(
                        new StringStorageValue(lexicalComponent.name()),
                        new ListStorageValue(List.of())
                    )
                ),
                new ListStorageValue(
                    new StringStorageValue("syntaxAnalyser"), 
                    new ListStorageValue(
                        new StringStorageValue(syntaxComponent.name()),
                        syntaxComponent.description()
                    )
                ),
                new ListStorageValue(
                    new StringStorageValue("codeGenerator"), 
                    new ListStorageValue(
                        new StringStorageValue(codeGeneratorComponent.name()),
                        codeGeneratorComponent.description()
                    )
                )
            ))
        ));
    }

    @Test
    public void produce_incorrectFormat_missingKey() {
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

        assertThrows(MissingKeyException.class, () -> factory.produce(
            new MapStorageValue(Map.of(
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
    public void produce_incorrectFormat_multipleMissingKeys() {
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

        assertThrows(MissingKeyException.class, () -> factory.produce(
            new MapStorageValue(Map.of(
                "syntaxAnalyser", new ListStorageValue(
                    new StringStorageValue(syntaxComponent.name()),
                    syntaxComponent.description()
                )
            ))
        ));
    }

    //Check internal (loaded) values
}
