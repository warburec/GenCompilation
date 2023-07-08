package semantic_analysis;

import java.util.*;

import code_generation.IdentifierGeneration;

public class TypeChecker implements SemanticAnalyser {
    
    protected Set<IdentifierGeneration> declaredIdentifiers;

    public TypeChecker() {
        declaredIdentifiers = new HashSet<>();
    }

    public boolean isDeclared(IdentifierGeneration identifier) {
        return declaredIdentifiers.contains(identifier);
    }

    public void declare(IdentifierGeneration identfier) {
        declaredIdentifiers.add(identfier);
    }
    
}
