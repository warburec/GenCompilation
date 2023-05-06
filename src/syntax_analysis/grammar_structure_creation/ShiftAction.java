package syntax_analysis.grammar_structure_creation;

import java.util.Map;

import grammar_objects.Token;

public record ShiftAction(Map<Token, State> shifts) implements Action {

    public State getState(Token token) throws SyntaxError {
        State stateFound = shifts.get(token);

        if(stateFound == null) {
            throw new SyntaxError(token);
        }

        return stateFound;
    }

}
