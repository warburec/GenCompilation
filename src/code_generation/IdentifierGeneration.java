package code_generation;

import grammar_objects.Identifier;

//TODO: Allow any dynamic token, Consider changes to LiteralGenerations too
public class IdentifierGeneration implements CodeElement {

    private Identifier identifier;

    public IdentifierGeneration(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getGeneration() {
        return identifier.getIdentifierName();
    }
    
    public String getType() {
        if(identifier.getType() == null) {
            return "";
        }
        
        return identifier.getType();
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
