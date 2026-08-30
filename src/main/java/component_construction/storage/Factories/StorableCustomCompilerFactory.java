package component_construction.storage.factories;

import java.util.*;

import code_generation.CodeGenerator;
import component_construction.storage.StorableCustomCompiler;
import component_construction.storage.dynamic_loading.*;
import component_construction.storage.exceptions.*;
import lexical_analysis.LexicalAnalyser;
import storage.storage_values.*;
import syntax_analysis.SyntaxAnalyser;

/**
 * A factory for producing StorableCustomCompilers.
 */
public class StorableCustomCompilerFactory implements Loader<StorableCustomCompiler> {
    protected ReflectiveClassConstructor reflectiveClassLoader = new ReflectiveClassConstructor();
    protected Map<String, Factory<LexicalAnalyser>> lexicalAnalyserFactoryRepository = new HashMap<>();
    protected Map<String, Factory<SyntaxAnalyser>> syntaxAnalyserFactoryRepository = new HashMap<>();
    protected Map<String, Factory<CodeGenerator>> codeGeneratorFactoryRepository = new HashMap<>();
    protected final Set<String> expectedKeys = Set.of(
        "lexicalAnalyser",
        "syntaxAnalyser",
        "codeGenerator"
    );

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
    /**
     * Builds a {@code StorableCustomCompiler} from a map of specified StorageValues.
     * 
     * The format of the loadValue for loading {@code StorableCustomCompiler} components is a {@code MapStorageValue} containing the keys "lexicalAnalyser", "syntaxAnalyser", and "codeGenerator" mapped to their respective {@code StorageValue<?>} descriptions
     * 
     * In order to be loadable, components must be one of: 
     *  - registered with a factory
     *  - {@code LoadableBy}
     *  - {@code Loadable}
     *  - Have a constructor taking a single {@code StorageValue<?>} parameter
     * @param loadValue The StorageValue to be used for building a {@code StorableCustomCompiler}
     * @return The produced {@code StorableCustomCompiler}
     * @throws IncorrectLoadValueFormat The provided load value was in an incorrect format
     * @throws MissingKeyException The provided load value was missing one or more required keys
     * @throws NonExistentComponentException A specified component class does not exist
     * @throws ComponentCastException A specified component class could not be cast to its intended type
     * @throws ReflectiveLoadFailure A specified component class does not contain a constructor taing a single {@code StorageValue<?>} parameter
     * @throws IncorrectLoadValueFormat A provided component's description was not the expected type {@code ListStorageValue}
     */
    public StorableCustomCompiler produce(StorageValue<?> loadValue) throws 
        IncorrectLoadValueFormat, 
        MissingKeyException, 
        NonExistentComponentException, 
        ComponentCastException,
        ReflectiveLoadFailure
    {
        Map<String, StorageValue<?>> mapValue = tryGetMapValue(loadValue);
        Set<String> keys = mapValue.keySet();

        if (!keys.containsAll(expectedKeys)) {
            Set<String> missingKeys = new HashSet<String>(expectedKeys);
            missingKeys.removeAll(keys);
            throw new MissingKeyException(missingKeys);
        }

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

    private Map<String, StorageValue<?>> tryGetMapValue(StorageValue<?> loadValue) throws IncorrectLoadValueFormat {
        MapStorageValue map;
        Map<String, StorageValue<?>> mapValue;
        
        try {
            map = (MapStorageValue) loadValue;
        }
        catch (ClassCastException e) {
            throw new IncorrectLoadValueFormat(
                "MapStorageValue which maps String -> ListStorageValue", 
                loadValue.getClass().getSimpleName(),
                e
            );
        }

        mapValue = map.getValue();
        return mapValue;
    }

    /**
     * Converts a given description into a {@code ComponentInformation} object
     * @param description The component's description as a {@code ListStorageValue}
     * @return The ComponentInformation representation of the description
     * @throws IncorrectLoadValueFormat The provided component description was not the expected type {@code ListStorageValue}
     */
    protected ComponentInformation getComponentDescription(StorageValue<?> description) throws IncorrectLoadValueFormat {
        List<StorageValue<?>> entry;

        try {
            entry = ((ListStorageValue)description).getValue();
        }
        catch (ClassCastException e) {
            throw new IncorrectLoadValueFormat(
                "MapStorageValue which maps String -> ListStorageValue",
                "MapStorageValue which maps String -> " + description.getClass().getSimpleName(),
                e
            );
        }

        return new ComponentInformation(
            ((StringStorageValue)entry.get(0)).getValue(),
            (ListStorageValue)entry.get(1)
        );
    }
    
    protected record ComponentInformation(String name, ListStorageValue description) {}

    @SuppressWarnings("unchecked")
    /**
     * Builds a component with the provided description.
     * Components must be: registered with a factory; LoadableBy; Loadable; Or, have a constructor taking a single {@code StorageValue<?>} parameter.
     * @param <T> The abstract type of the component
     * @param repository The repository to query for registered factories for the specific component type
     * @param description The description with which to load the component with
     * @param selectedType The subclass with which to produce and use the component
     * @return The built component
     * @throws NonExistentComponentException The specified component class does not exist
     * @throws ComponentCastException The specified component class could not be cast to the selectedType
     * @throws ReflectiveLoadFailure The specified component class does not contain a constructor taing a single {@code StorageValue<?>} parameter
     * @throws IncorrectLoadValueFormat The provided component description was not the expected type {@code ListStorageValue}
     */
    protected <T> T buildComponent(
        Map<String, Factory<T>> repository, 
        StorageValue<?> description, 
        Class<T> selectedType
    ) throws 
        NonExistentComponentException, 
        ComponentCastException, 
        ReflectiveLoadFailure,
        IncorrectLoadValueFormat
    {
        ComponentInformation componentDescription = getComponentDescription(description);

        if (repository.containsKey(componentDescription.name()))
            return repository
                .get(componentDescription.name())
                .produce(componentDescription.description());

        Class<T> clazz;
        
        try {
            clazz = (Class<T>) Class
                .forName(componentDescription.name())
                .asSubclass(selectedType);
        } 
        catch (ClassNotFoundException e) {
            throw new NonExistentComponentException(componentDescription.name());
        }
        catch (ClassCastException e) {
            throw new ComponentCastException(componentDescription.name(), selectedType.getName());
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
            } 
            catch (ClassNotFoundException | IllegalArgumentException e) {
                // ClassNotFoundException cannot occur as the cast succeeded earlier in this method 
                // There are also no arguments to cause an IllegalArgumentException
                return null;
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
        } 
        catch (ClassNotFoundException e) {
            // ClassNotFoundException cannot occur as the cast succeeded earlier in this method
            return null;
        } 
        catch (IllegalArgumentException e) {
            throw new ReflectiveLoadFailure("Failed to construct an object of class \"" + componentDescription.name() + "\" as no constructor exists taking \"StorageValue<?>\" as its only parameter.", e);
        }
    }
}
