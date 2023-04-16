package syntax_analysis.grammar_structure_creation;

import grammar_objects.ProductionRule;

public record ReduceAction(ProductionRule reductionRule) implements Action {
    
}
