package semantic_analysis;

import java.util.HashSet;
import java.util.Set;

import code_generation.IdentifierGeneration;

public class TypeChecker implements SemanticAnalyser {
    
    protected Set<IdentifierGeneration> declaredIdentifiers;

    public TypeChecker() {
        declaredIdentifiers = new HashSet<>();
    }

    public boolean isDeclared(IdentifierGeneration identifier) {
        return declaredIdentifiers.contains(identifier);
    }

    public void setDeclared(IdentifierGeneration identfier) {
        declaredIdentifiers.add(identfier);
    }
    
}
