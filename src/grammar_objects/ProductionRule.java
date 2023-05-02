package grammar_objects;

public record ProductionRule(NonTerminal nonTerminal, LexicalElement[] productionSequence) {
    
    public LexicalElement getFirstElement() {
        return productionSequence[0];
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProductionRule)) {
            return false;
        }
         
        ProductionRule otherRule = (ProductionRule)obj;

        if(nonTerminal == null) {
            if(otherRule.nonTerminal() != null) { return false; }
        }
        else {
            if(!nonTerminal.equals(otherRule.nonTerminal())) { return false; }
        }

        if(productionSequence.length != otherRule.productionSequence().length) { return false; }

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
        
        if(nonTerminal == null) {
            string += "null";
        }
        else {
            string += "<" + nonTerminal.toString() + "> := ";
        }

        for (LexicalElement lexicalElement : productionSequence) {
            if(lexicalElement == null) { string += "null";  }

            if(lexicalElement instanceof NonTerminal) {
                string += "<" +lexicalElement.toString() + "> ";
            }
            else {
                string += lexicalElement.toString() + " ";
            }
        }

        string.stripTrailing();
        return string;
    }
}
