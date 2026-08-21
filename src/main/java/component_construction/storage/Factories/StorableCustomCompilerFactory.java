package component_construction.storage.factories;

import java.util.*;

import code_generation.CodeGenerator;
import component_construction.storage.StorableCustomCompiler;
import component_construction.storage.dynamic_loading.*;
import lexical_analysis.LexicalAnalyser;
import storage.storage_values.*;
import syntax_analysis.SyntaxAnalyser;

public class StorableCustomCompilerFactory implements Loader<StorableCustomCompiler> {
    protected ReflectiveClassConstructor reflectiveClassLoader = new ReflectiveClassConstructor();
    protected Map<String, Factory<LexicalAnalyser>> lexicalAnalyserFactoryRepository = new HashMap<>();
    protected Map<String, Factory<SyntaxAnalyser>> syntaxAnalyserFactoryRepository = new HashMap<>();
    protected Map<String, Factory<CodeGenerator>> codeGeneratorFactoryRepository = new HashMap<>();

    public StorableCustomCompilerFactory addLexicalAnalyserFactory(String name, Factory<LexicalAnalyser> factory) {
        lexicalAnalyserFactoryRepository.put(name, factory);
        return this;
    }

    public StorableCustomCompilerFactory addSyntaxAnalyserFactory(String name, Factory<SyntaxAnalyser> factory) {
        syntaxAnalyserFactoryRepository.put(name, factory);
        return this;
    }

    public StorableCustomCompilerFactory addCodeGeneratorFactory(String name, Factory<CodeGenerator> factory) {
        codeGeneratorFactoryRepository.put(name, factory);
        return this;
    }

    @Override
    // TODO: Explain requirements for storage/loading
    public StorableCustomCompiler produce(StorageValue<?> loadValue) {
        MapStorageValue map = (MapStorageValue) loadValue;
        Map<String, StorageValue<?>> mapValue = map.getValue();

        return new StorableCustomCompiler(
            buildComponent(
                lexicalAnalyserFactoryRepository, 
                mapValue.get("lexicalAnalyser"),
                LexicalAnalyser.class
            ),
            buildComponent(
                syntaxAnalyserFactoryRepository, 
                mapValue.get("syntaxAnalyser"),
                SyntaxAnalyser.class
            ),
            buildComponent(
                codeGeneratorFactoryRepository, 
                mapValue.get("codeGenerator"),
                CodeGenerator.class
            )
        );
    }

    // TODO: Handle errors
    protected ComponentInformation getComponentDescription(StorageValue<?> description) {
        List<StorageValue<?>> entry = ((ListStorageValue)description).getValue();

        return new ComponentInformation(
            ((StringStorageValue)entry.get(0)).getValue(),
            (ListStorageValue)entry.get(1)
        );
    }
    
    protected record ComponentInformation(String name, ListStorageValue description) {}

    @SuppressWarnings("unchecked")
    /**
     * Components must be: registered with a factory; LoadableBy; Loadable; Or, have a constructor taking the exact parameters provided.
     * @param <T>
     * @param repository
     * @param description
     * @param selectedType
     * @return
     */
    protected <T> T buildComponent(Map<String, Factory<T>> repository, StorageValue<?> description, Class<T> selectedType) {
        ComponentInformation componentDescription = getComponentDescription(description);

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

        if (ReflectivelyLoadable.class.isAssignableFrom(clazz)) {
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

            ((ReflectivelyLoadable)component).load(componentDescription.description());

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
