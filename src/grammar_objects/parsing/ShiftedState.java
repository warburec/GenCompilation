package grammar_objects.parsing;

import grammar_objects.fundamentals.Token;
import grammar_objects.grammar_structure_creation.State;

public record ShiftedState(State state, Token tokenUsed) implements ParseState {

}
