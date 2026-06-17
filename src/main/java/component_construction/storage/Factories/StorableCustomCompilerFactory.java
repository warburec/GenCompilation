package component_construction.storage.Factories;

import java.util.*;

import component_construction.storage.*;
import storage.storage_values.*;
import component_construction.custom_components.*;

public class StorableCustomCompilerFactory implements Factory<StorableCustomCompiler> {
    ReflectiveFactoryRepository<CustomLexicalAnalyser> lexicalAnalyserFactoryRepository = new ReflectiveFactoryRepository<>(CustomLexicalAnalyser.class);
    ReflectiveFactoryRepository<CustomSyntaxAnalyser> syntaxAnalyserFactoryRepository = new ReflectiveFactoryRepository<>(CustomSyntaxAnalyser.class);;
    ReflectiveFactoryRepository<CustomCodeGenerator> codeGeneratorFactoryRepository = new ReflectiveFactoryRepository<>(CustomCodeGenerator.class);;

    @Override
    // TODO: Explain requirements for storage/loading
    public StorableCustomCompiler produce(StorageValue<?> loadValue) {
        MapStorageValue map = (MapStorageValue) loadValue;
        Map<String, StorageValue<?>> mapValue = map.getValue();

        CustomLexicalAnalyser customLexicalAnalyser;
        CustomSyntaxAnalyser customSyntaxAnalyser;
        CustomCodeGenerator customCodeGenerator;

        try {
            ComponentDescription lexicalAnalyserDescription = getComponentDescription(mapValue.get("lexicalAnalyser"));

            customLexicalAnalyser = lexicalAnalyserFactoryRepository.instantiate(
                lexicalAnalyserDescription.name,
                lexicalAnalyserDescription.description
            );
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // TODO: Create custom exception
        }

        try {
            ComponentDescription syntaxAnalyserDescription = getComponentDescription(mapValue.get("syntaxAnalyser"));

            customSyntaxAnalyser = syntaxAnalyserFactoryRepository.instantiate(
                syntaxAnalyserDescription.name,
                syntaxAnalyserDescription.description
            );
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // TODO: Create custom exception
        }

        try {
            ComponentDescription codeGeneratorDescription = getComponentDescription(mapValue.get("syntaxAnalyser"));

            customCodeGenerator = codeGeneratorFactoryRepository.instantiate(
                codeGeneratorDescription.name,
                codeGeneratorDescription.description
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

    // TODO: Handle errors
    private ComponentDescription getComponentDescription(StorageValue<?> description) {
        List<StorageValue<?>> entry = ((ListStorageValue)description).getValue();

        return new ComponentDescription(
            ((StringStorageValue)entry.get(0)).getValue(),
            (ListStorageValue)entry.get(1)
        );
    }
    
    private record ComponentDescription(String name, ListStorageValue description) {}
}
