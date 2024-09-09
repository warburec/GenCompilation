package grammar_objects;

public class EOF extends Token {

    public EOF() {
        super(null);
    }
    
    @Override
    public String toString() {
        return "$";
    }
}
