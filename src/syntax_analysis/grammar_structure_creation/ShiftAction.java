package syntax_analysis.grammar_structure_creation;

import java.util.Map;

import grammar_objects.Token;

public record ShiftAction(Map<Token, State> shifts) implements Action {

    public State getState(Token token) {
        State stateFound = shifts.get(token);

        if(stateFound == null) {
            throw new UnsupportedShiftException(token);
        }

        return stateFound;
    }

    public class UnsupportedShiftException extends UnsupportedOperationException {

        public UnsupportedShiftException(Token token) {
            super("Shift for \"" + token.getName() + "\" is not supported by the grammar supplied");
        }

    }

}
