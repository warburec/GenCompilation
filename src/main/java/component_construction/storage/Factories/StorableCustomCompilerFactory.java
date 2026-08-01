package component_construction.storage.factories;

import java.util.*;

import component_construction.custom_components.*;
import component_construction.storage.StorableCustomCompiler;
import component_construction.storage.dynamic_loading.*;
import storage.external_interfaces.Loadable;
import storage.storage_values.*;

public class StorableCustomCompilerFactory implements Loader<StorableCustomCompiler> {
    protected ReflectiveClassConstructor reflectiveClassLoader = new ReflectiveClassConstructor();
    protected Map<String, Factory<CustomLexicalAnalyser>> lexicalAnalyserFactoryRepository = new HashMap<>();
    protected Map<String, Factory<CustomSyntaxAnalyser>> syntaxAnalyserFactoryRepository = new HashMap<>();
    protected Map<String, Factory<CustomCodeGenerator>> codeGeneratorFactoryRepository = new HashMap<>();

    @Override
    // TODO: Explain requirements for storage/loading
    public StorableCustomCompiler produce(StorageValue<?> loadValue) {
        MapStorageValue map = (MapStorageValue) loadValue;
        Map<String, StorageValue<?>> mapValue = map.getValue();

        CustomLexicalAnalyser customLexicalAnalyser = buildComponent(
            lexicalAnalyserFactoryRepository, 
            mapValue.get("lexicalAnalyser"),
            CustomLexicalAnalyser.class
        );
        CustomSyntaxAnalyser customSyntaxAnalyser = buildComponent(
            syntaxAnalyserFactoryRepository, 
            mapValue.get("syntaxAnalyser"),
            CustomSyntaxAnalyser.class
        );
        CustomCodeGenerator customCodeGenerator = buildComponent(
            codeGeneratorFactoryRepository, 
            mapValue.get("codeGenerator"),
            CustomCodeGenerator.class
        );

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
    
    @SuppressWarnings("unchecked")
    protected <T extends Loadable> T buildComponent(Map<String, Factory<T>> repository, StorageValue<?> description, Class<T> selectedType) {
        ComponentDescription componentDescription = getComponentDescription(description);

        if (repository.containsKey(componentDescription.name())) {
            return repository
                .get(componentDescription.name())
                .produce(componentDescription.description());
        }

        Class<T> clazz;
        
        try {
            clazz = (Class<T>) Class
                .forName(componentDescription.name())
                .asSubclass(selectedType);
        } 
        catch (ClassNotFoundException e) {
            //TODO
            throw new RuntimeException();
        }
        catch (ClassCastException e) {
            //TODO
            throw new RuntimeException();
        }

        if (LoadableBy.class.isAssignableFrom(clazz)) {
            Class<LoadableBy<Loader<T>>> loadableBy = (Class<LoadableBy<Loader<T>>>) clazz.asSubclass(LoadableBy.class);
            Class<Loader<T>> targetLoader = LoadableBy.getTargetLoader(loadableBy);
            Loader<T> loader = Loader.construct(targetLoader);
            return loader.produce(componentDescription.description());
        }

        if (Loadable.class.isAssignableFrom(clazz)) {
            T component;

            try {
                component = reflectiveClassLoader.construct(
                    componentDescription.name(),
                    selectedType
                );
            } catch (ClassNotFoundException | IllegalArgumentException e) {
                //TODO
                throw new RuntimeException();
            }

            component.load(componentDescription.description());

            return component;
        }

        // Use constructor
        try {
            return reflectiveClassLoader.construct(
                componentDescription.name(),
                selectedType,
                StorageValue.class,
                componentDescription.description()
            );
        } catch (ClassNotFoundException | IllegalArgumentException e) {
            //TODO
            throw new RuntimeException();
        }
    }
}
