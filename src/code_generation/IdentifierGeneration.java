package code_generation;

import grammar_objects.Identifier;

public class IdentifierGeneration implements CodeElement {

    private Identifier identifier;

    public IdentifierGeneration(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getGeneration() {
        return identifier.getIdentifierName();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IdentifierGeneration)) { return false; }

        IdentifierGeneration otherGeneration = (IdentifierGeneration)obj;
        return identifier.exactlyEquals(otherGeneration.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
