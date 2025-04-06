package grammar_objects;

public record ProductionRule(NonTerminal nonTerminal, LexicalElement[] productionSequence) {
    
    public LexicalElement getFirstElement() {
        return productionSequence[0];
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProductionRule)) { return false; }
         
        ProductionRule otherRule = (ProductionRule)obj;

        if (nonTerminal == null && otherRule.nonTerminal() != null) { return false; }
        if (!nonTerminal.equals(otherRule.nonTerminal())) { return false; }
        if (productionSequence.length != otherRule.productionSequence().length) { return false; }

        LexicalElement[] otherProductionSequence = otherRule.productionSequence();
        for (int i = 0; i < productionSequence.length; i++) {
            if(!productionSequence[i].equals(otherProductionSequence[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        String string = "";
        
        if (nonTerminal == null) {
            string += "null";
        }
        else {
            string += nonTerminal.toString();
        }

        string += " := ";

        for (int i = 0; i < productionSequence.length - 1; i++) {
            if (productionSequence[i] == null) { string += "null"; }
            
            string += productionSequence[i].toString() + " ";
        }

        if (productionSequence.length > 0) {
            string += productionSequence[productionSequence.length - 1].toString();
        }

        string.stripTrailing();
        return string;
    }

    @Override
    public int hashCode() {
        int hashCode = nonTerminal.hashCode() * 31;

        for (LexicalElement lexicalElement : productionSequence) {
            hashCode *= lexicalElement.hashCode();
        }

        return hashCode;
    }
}
