package syntax_analysis.parsing;

import grammar_objects.Token;
import syntax_analysis.grammar_structure_creation.State;

public record ShiftedState(State state, Token tokenUsed) implements ParseState {

}
