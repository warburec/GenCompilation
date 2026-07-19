package component_construction.storage.dynamic_loading;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectiveClassConstructor {

    /**
     * Constructs an object of the specified name and returns it as the target type, using reflection.
     * @param <T> The output target type
     * @param className The name of the class to be loaded. Provided following the format provided by {@code someObject.class.getName()}
     * @param targetType The specified target output type
     * @return The constructed object of the target type
     * @throws ClassNotFoundException The specified class could not be found
     * @throws IllegalArgumentException One or more of the provided arguments has caused a failure to construct the specifed class
     */
    public <T> T construct(
        String className, 
        Class<T> targetType
    ) 
    throws 
        ClassNotFoundException,
        IllegalArgumentException
    {  
        return construct(
            className, 
            targetType, 
            new Class<?>[] {},
            new Object[] {}
        );
    }

    /**
     * Constructs an object of the specified name and returns it as the target type, using reflection.
     * @param <T> The output target type
     * @param className The name of the class to be loaded. Provided following the format provided by {@code someObject.class.getName()}
     * @param targetType The specified target output type
     * @param parameterType The type expected by the constructor for the specified class
     * @param parameter The value to be passed to the constructor of the specified class. Note: Inner classes require an additional initial parameter to be a parent object of the outer class
     * @return The constructed object of the target type
     * @throws ClassNotFoundException The specified class could not be found
     * @throws IllegalArgumentException One or more of the provided arguments has caused a failure to construct the specifed class
     */
    public <T> T construct(
        String className, 
        Class<T> targetType, 
        Class<?> parameterType, 
        Object parameter
    ) 
    throws 
        ClassNotFoundException,
        IllegalArgumentException
    {   
        return construct(
            className, 
            targetType, 
            new Class<?>[] { parameterType },
            new Object[] { parameter }
        );
    }

    /**
     * Constructs an object of the specified name and returns it as the target type, using reflection.
     * @param <T> The output target type
     * @param className The name of the class to be loaded. Provided following the format provided by {@code someObject.class.getName()}
     * @param targetType The specified target output type
     * @param parameterTypes The types expected by the constructor for the specified class
     * @param parameters The values to be passes to the constructor of the specified class. Note: Inner classes require an additional initial parameter to be a parent object of the outer class
     * @return The constructed object of the target type
     * @throws ClassNotFoundException The specified class could not be found
     * @throws IllegalArgumentException One or more of the provided arguments has caused a failure to construct the specifed class
     */
    public <T> T construct(
        String className, 
        Class<T> targetType, 
        Class<?>[] parameterTypes, 
        Object[] parameters
    ) 
    throws 
        ClassNotFoundException,
        IllegalArgumentException
    {                
        try {
            return Class
                .forName(className)
                .asSubclass(targetType)
                .getDeclaredConstructor(parameterTypes)
                .newInstance(parameters);
        } 
        catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Target class not found: " + className, e);
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                "No constructor found in " + className + " matching types: [" + getTypesString(parameterTypes) + "]",
                e
            );
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Type mismatch between declared types [" + getTypesString(parameterTypes) + "] and parameter types [" + getParameterTypesString(parameters) + "]",
                e
            );
        }
        catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Constructor is private or inaccessible in class: " + className, e);
        }
        catch (InstantiationException e) {
            throw new IllegalArgumentException("Cannot instantiate abstract class or interface: " + className, e);
        }
        catch (InvocationTargetException e) {
            throw new IllegalArgumentException("The constructor for " + className + " threw an exception", e.getCause());
        }
    }

    protected String getTypesString(Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes)
            .map(Class::getSimpleName)
            .collect(Collectors.joining(", "));
    }

    protected String getParameterTypesString(Object[] parameters) {
        return Arrays.stream(parameters)
            .map(parameter -> parameter.getClass().getSimpleName())
            .collect(Collectors.joining(", "));
    }
}
