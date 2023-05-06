package syntax_analysis.grammar_structure_creation;

import grammar_objects.Token;

public class SyntaxError extends UnsupportedOperationException {

    private Token token;

    public SyntaxError(Token token) {
        super("Shift for \"" + token.getName() + "\" is not supported by the grammar supplied");
    }

    @Override
    public String getMessage() {
        String tokenPosition = token.getPositionString();

        if(tokenPosition == null) { return super.getMessage(); }

        return "Unexpected token \"" + token.getName() + "\" at " + tokenPosition;
    }
}