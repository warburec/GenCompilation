package helper_objects;

import java.util.Collection;

public class ToStringFormatting {

    /**
     * Creates a new line and adds indentation before the string representation of each value in the collection.
     * @param <E> The type which the given collection contains
     * @param collection The collection provided
     * @return The produced string representation of the collection
     */
    public static <E> String indentFormat(Collection<E> collection) {
        String out = "";

        for (Object object : collection) {
            out += "\t" + object.toString().replace("\n", "\n\t") + "\n";
        }

        return out.stripTrailing();
    }

    /**
     * Creates a new line and adds indentation before the string representation of the given value.
     * @param <E> The type of the provided value
     * @param value The value provided
     * @return The produced string representation of the value
     */
    public static <E> String indentFormat(E value) {
        return "\t" + value.toString().replace("\n", "\n\t");
    }
    
}
