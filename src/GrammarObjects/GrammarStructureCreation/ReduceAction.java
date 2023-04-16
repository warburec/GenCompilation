package GrammarObjects.GrammarStructureCreation;

import GrammarObjects.Fundamentals.ProductionRule;

public record ReduceAction(ProductionRule reductionRule) implements Action {
    
}
