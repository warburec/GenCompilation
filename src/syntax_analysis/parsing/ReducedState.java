package syntax_analysis.parsing;

import java.util.*;

import grammar_objects.ProductionRule;
import syntax_analysis.grammar_structure_creation.State;

public record ReducedState(State state, ProductionRule reductionRule, List<ParseState> statesReduced) implements ParseState {

    public ReducedState(State state, ProductionRule reductionRule) {
        this(state, reductionRule, new ArrayList<>()); 
    }

}
