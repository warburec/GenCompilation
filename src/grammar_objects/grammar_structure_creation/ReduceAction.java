package grammar_objects.grammar_structure_creation;

import grammar_objects.fundamentals.ProductionRule;

public record ReduceAction(ProductionRule reductionRule) implements Action {
    
}
