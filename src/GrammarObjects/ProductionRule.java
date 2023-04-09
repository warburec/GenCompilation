package GrammarObjects;

public record ProductionRule(NonTerminal nonTerminal, LexicalElement[] productionSequence) {
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProductionRule)) {
            return false;
        }
         
        ProductionRule otherRule = (ProductionRule) obj;

        if(!nonTerminal.equals(otherRule.nonTerminal())) { return false; }
        if(productionSequence.length != otherRule.productionSequence().length) { return false; }

        LexicalElement[] otherProductionSequence = otherRule.productionSequence();
        for (int i = 0; i < productionSequence.length; i++) {
            if(!productionSequence[i].equals(otherProductionSequence[i])) {
                return false;
            }
        }

        return true;
    }

}
