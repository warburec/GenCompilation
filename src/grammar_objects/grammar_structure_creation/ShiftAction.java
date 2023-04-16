package grammar_objects.grammar_structure_creation;

import java.util.Map;

import grammar_objects.fundamentals.*;

public record ShiftAction(Map<Token, State> shifts) implements Action {
    
}
