package code_generation;

import grammar_objects.Literal;

public class LiteralGeneration implements CodeElement {

    private Literal literal;

    public LiteralGeneration(Literal literal) {
        this.literal = literal;
    }

    @Override
    public String getGeneration() {
        return literal.getValue();
    }

}
