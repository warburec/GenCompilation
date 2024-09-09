package semantic_analysis;

import java.util.*;

import code_generation.CodeElement;

public class TypeChecker implements SemanticAnalyser {
    
    protected Set<CodeElement> declaredElements;

    public TypeChecker() {
        declaredElements = new HashSet<>();
    }

    public boolean isDeclared(CodeElement identifier) {
        return declaredElements.contains(identifier);
    }

    public void declare(CodeElement identfier) {
        declaredElements.add(identfier);
    }
    
}
