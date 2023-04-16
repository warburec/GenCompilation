package syntax_analysis.parsing;

import grammar_objects.ProductionRule;
import syntax_analysis.grammar_structure_creation.State;

public record ReducedState(State state, ProductionRule reductionRule) implements ParseState {

}
