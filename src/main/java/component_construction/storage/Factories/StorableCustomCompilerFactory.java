package component_construction.storage.Factories;

import java.util.Map;
import component_construction.storage.*;
import storage.storage_values.*;
import component_construction.custom_components.*;

public class StorableCustomCompilerFactory implements Factory<StorableCustomCompiler> {
    ReflectiveFactoryRepository<CustomLexicalAnalyser> lexicalAnalyserFactoryRepository = new ReflectiveFactoryRepository<>();
    ReflectiveFactoryRepository<CustomSyntaxAnalyser> syntaxAnalyserFactoryRepository = new ReflectiveFactoryRepository<>();
    ReflectiveFactoryRepository<CustomCodeGenerator> codeGeneratorFactoryRepository = new ReflectiveFactoryRepository<>();

    @Override
    public StorableCustomCompiler produce(StorageValue<?> loadValue) {
        MapStorageValue map = (MapStorageValue) loadValue;
        Map<String, StorageValue<?>> mapValue = map.getValue();

        CustomLexicalAnalyser customLexicalAnalyser;
        CustomSyntaxAnalyser customSyntaxAnalyser;
        CustomCodeGenerator customCodeGenerator;

        try {
            customLexicalAnalyser = lexicalAnalyserFactoryRepository.instantiate(
                "lexicalAnalyser", 
                mapValue.get("lexicalAnalyser")
            );
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // TODO: Create custom exception
        }

        try {
            customSyntaxAnalyser = syntaxAnalyserFactoryRepository.instantiate(
                "syntaxAnalyser", 
                mapValue.get("syntaxAnalyser")
            );
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // TODO: Create custom exception
        }

        try {
            customCodeGenerator = codeGeneratorFactoryRepository.instantiate(
                "codeGenerator", 
                mapValue.get("codeGenerator")
            );
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // TODO: Create custom exception
        }

        return new StorableCustomCompiler(
            customLexicalAnalyser,
            customSyntaxAnalyser,
            customCodeGenerator
        );
    }
    
}
