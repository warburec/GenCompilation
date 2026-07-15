package component_construction.storage.dynamic_loading;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectiveClassConstructor {

    //TODO: Fix exceptions and add to method declarations
    //TODO: Document

    public <T> T construct(String className, Class<T> targetType) throws ClassNotFoundException {
        return construct(
            className, 
            targetType, 
            new Class<?>[] {},
            new Object[] {}
        );
    }

    public <T> T construct(String className, Class<T> targetType, Class<?> parameterType, Object parameter) throws ClassNotFoundException {
        return construct(
            className, 
            targetType, 
            new Class<?>[] { parameterType },
            new Object[] { parameter }
        );
    }

    public <T> T construct(String className, Class<T> targetType, Class<?>[] parameterTypes, Object[] parameters) throws ClassNotFoundException {
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
            throw new RuntimeException("Constructor is private or inaccessible in class: " + className, e);
        }
        catch (InstantiationException e) {
            throw new RuntimeException("Cannot instantiate abstract class or interface: " + className, e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException("The constructor for " + className + " threw an exception", e.getCause());
        }
        catch (SecurityException e) {
            throw new RuntimeException("Reflection configuration error for class: " + className, e);
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
