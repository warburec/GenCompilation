package syntax_analysis.grammar_structure_creation;

import java.util.Map;

import grammar_objects.Token;

public record ShiftAction(Map<Token, State> shifts) implements Action {
    
}
