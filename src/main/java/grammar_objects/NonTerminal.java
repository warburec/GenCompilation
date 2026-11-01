package grammar_objects;

public class NonTerminal extends LexicalElement {

    public NonTerminal(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "<" + this.getName() + ">";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NonTerminal)) { return false; }
        return super.equals(obj);
    }
}
