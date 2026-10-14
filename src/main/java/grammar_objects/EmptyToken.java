package grammar_objects;

import java.util.*;

public class EmptyToken extends Token {
    
    public static final Set<Token> EquivalentTokens = Set.of(
        new Token("")
    );

    public EmptyToken() {
        super("");
    }

    @Override
    public String toString() {
        return "ε";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) { return false; }
         
        Token otherToken = (Token) obj;
        return EquivalentTokens.contains(otherToken);
    }
    
}
