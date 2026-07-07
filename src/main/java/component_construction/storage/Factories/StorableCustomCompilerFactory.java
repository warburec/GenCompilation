package component_construction.storage.factories;

import java.util.*;
import component_construction.custom_components.*;
import component_construction.storage.*;
import component_construction.storage.dynamic_loading.*;
import storage.external_interfaces.Loadable;
import storage.storage_values.*;

public class StorableCustomCompilerFactory implements Loader<StorableCustomCompiler> {
    protected ReflectiveClassConstructor reflectiveClassLoader = new ReflectiveClassConstructor();
    protected FactoryRepository<CustomLexicalAnalyser> lexicalAnalyserFactoryRepository = new FactoryRepository<>();
    protected FactoryRepository<CustomSyntaxAnalyser> syntaxAnalyserFactoryRepository = new FactoryRepository<>();
    protected FactoryRepository<CustomCodeGenerator> codeGeneratorFactoryRepository = new FactoryRepository<>();

    @SuppressWarnings("unchecked")
    @Override
    // TODO: Explain requirements for storage/loading
    public StorableCustomCompiler produce(StorageValue<?> loadValue) {
        MapStorageValue map = (MapStorageValue) loadValue;
        Map<String, StorageValue<?>> mapValue = map.getValue();

        CustomLexicalAnalyser customLexicalAnalyser;
        CustomSyntaxAnalyser customSyntaxAnalyser;
        CustomCodeGenerator customCodeGenerator;

        //TODO: Abstract common code into easy to use method
        try {
            ComponentDescription lexicalAnalyserDescription = getComponentDescription(mapValue.get("lexicalAnalyser"));

            if (lexicalAnalyserFactoryRepository.containsKey(lexicalAnalyserDescription.name())) {
                customLexicalAnalyser = lexicalAnalyserFactoryRepository.instantiate(
                    lexicalAnalyserDescription.name(),
                    lexicalAnalyserDescription.description()
                );
            }

            Class<CustomLexicalAnalyser> clazz = (Class<CustomLexicalAnalyser>) Class
                .forName(lexicalAnalyserDescription.name())
                .asSubclass(CustomLexicalAnalyser.class);

            if (LoadableBy.class.isAssignableFrom(clazz)) {
                Class<LoadableBy<Loader<CustomLexicalAnalyser>>> loadableBy = (Class<LoadableBy<Loader<CustomLexicalAnalyser>>>) clazz.asSubclass(LoadableBy.class);
                Class<Loader<CustomLexicalAnalyser>> targetLoader = LoadableBy.getTargetLoader(loadableBy);
                Loader<CustomLexicalAnalyser> loader = Loader.construct(targetLoader);
                customLexicalAnalyser = loader.produce(lexicalAnalyserDescription.description());
            }
            else if (Loadable.class.isAssignableFrom(clazz)) {
                customLexicalAnalyser = reflectiveClassLoader.construct(
                    lexicalAnalyserDescription.name(),
                    CustomLexicalAnalyser.class
                );

                ((Loadable)customLexicalAnalyser)
                    .load(lexicalAnalyserDescription.description());
            }
            // Use constructor
            else {
                customLexicalAnalyser = reflectiveClassLoader.construct(
                    lexicalAnalyserDescription.name(),
                    CustomLexicalAnalyser.class,
                    StorageValue.class,
                    lexicalAnalyserDescription.description()
                );
            }
        }
        catch (ClassNotFoundException e) { //TODO: Check for other exceptions
            throw new RuntimeException(e); // TODO: Create custom exception
        }

        try {
            ComponentDescription syntaxAnalyserDescription = getComponentDescription(mapValue.get("syntaxAnalyser"));

            customSyntaxAnalyser = syntaxAnalyserFactoryRepository.instantiate(
                syntaxAnalyserDescription.name(),
                syntaxAnalyserDescription.description()
            );
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // TODO: Create custom exception
        }

        try {
            ComponentDescription codeGeneratorDescription = getComponentDescription(mapValue.get("syntaxAnalyser"));

            customCodeGenerator = codeGeneratorFactoryRepository.instantiate(
                codeGeneratorDescription.name(),
                codeGeneratorDescription.description()
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
    protected ComponentDescription getComponentDescription(StorageValue<?> description) {
        List<StorageValue<?>> entry = ((ListStorageValue)description).getValue();

        return new ComponentDescription(
            ((StringStorageValue)entry.get(0)).getValue(),
            (ListStorageValue)entry.get(1)
        );
    }
    
    protected record ComponentDescription(String name, ListStorageValue description) {}
}
