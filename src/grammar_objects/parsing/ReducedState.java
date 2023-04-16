package grammar_objects.parsing;

import grammar_objects.fundamentals.ProductionRule;
import grammar_objects.grammar_structure_creation.State;

public record ReducedState(State state, ProductionRule reductionRule) implements ParseState {

}
