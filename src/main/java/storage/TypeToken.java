package storage;

import java.lang.reflect.*;

public abstract class TypeToken<T> {
    private Type type;

    private TypeToken(){
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

    public static <T> TypeToken<T> instantiate() {
        return new TypeToken<T>() {};
    }
}
