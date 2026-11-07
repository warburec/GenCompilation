package grammar_objects;

public class EOF extends Token {

    public EOF() {
        super(null);
    }
    
    @Override
    public String toString() {
        return "$";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EOF;
    }

}
