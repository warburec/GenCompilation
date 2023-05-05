package code_generation;

import grammar_objects.Token;

public class TokenGeneration implements CodeElement {

    private String code;

    public TokenGeneration(Token token) {
        this.code = token.getName();
    }

    @Override
    public String getGeneration() {
        return code;
    }
    
}
