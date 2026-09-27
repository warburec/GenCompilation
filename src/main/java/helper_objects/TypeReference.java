package helper_objects;

import java.lang.reflect.*;

/**
 * A class for holding compile-time type information during runtime. 
 * Implementation of the "Type Token" design pattern.
 */
public abstract class TypeReference<T> {
    private Type type;

    private TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getContainedClass() {
        return (Class<T>) type.getClass();
    }

    public static <T> TypeReference<T> instantiate() {
        return new TypeReference<T>() {};
    }
}
