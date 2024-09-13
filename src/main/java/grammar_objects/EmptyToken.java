package grammar_objects;

public class EmptyToken extends Token{
    
    public EmptyToken() {
        super("");
    }

    @Override
    public String toString() {
        return "ε";
    }
}
