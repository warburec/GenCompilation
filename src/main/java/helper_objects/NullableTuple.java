package helper_objects;

public record NullableTuple<T, E>(T value1, E value2) implements Tuple<T,E> {

}
