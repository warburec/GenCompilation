package helperObjects;

public record NotEmptyTuple<T, E>(T value1, E value2) implements Tuple<T,E> {
    
    public NotEmptyTuple(T value1, E value2) {
        if(value1 == null || value2 == null) {
            throw new IllegalArgumentException("Values must not be null");
        }

        if(value1 instanceof String) {
            if(((String)value1).isBlank()) {
                throw new IllegalArgumentException("String values must not be empty");
            }
        }

        if(value2 instanceof String) {
            if(((String)value2).isBlank()) {
                throw new IllegalArgumentException("String values must not be empty");
            }
        }

        this.value1 = value1;
        this.value2 = value2;
    }

}
