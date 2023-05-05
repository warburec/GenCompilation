package syntax_analysis.grammar_structure_creation;

import grammar_objects.Token;

public class UnsupportedShiftException extends UnsupportedOperationException {

    public UnsupportedShiftException(Token token) {
        super("Shift for \"" + token.getName() + "\" is not supported by the grammar supplied");
    }

}