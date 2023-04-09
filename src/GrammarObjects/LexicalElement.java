package GrammarObjects;

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
        if (!(obj instanceof LexicalElement)) {
            return false;
        }
         
        LexicalElement otherElement = (LexicalElement) obj;

        return getName().equals(otherElement.getName());
    }
}
