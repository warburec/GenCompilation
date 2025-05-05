package grammar_objects;

public abstract class LexicalElement {
    private String name;

    public LexicalElement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!this.getClass().isInstance(obj)) { return false; }
         
        LexicalElement otherElement = (LexicalElement) obj;

        if (name == null) { return otherElement.getName() == null; }

        return getName().equals(otherElement.getName());
    }

    @Override
    public int hashCode() {
        if (name == null) { return 0; }
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
