package code_generation;

import grammar_objects.Identifier;

public class IdentifierGeneration implements CodeElement {

    private Identifier identifier;
    private boolean declared = false;

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
    
    public boolean isDeclared() {
        return declared;
    }

    public void setDeclared() {
        declared = true;
    }
}
