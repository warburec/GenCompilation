package grammar_objects;

import java.util.*;

//TODO: Test
public record Grammar(
    Set<Token> tokens,
    Set<NonTerminal> nonTerminals,
    List<ProductionRule> productionRules,
    NonTerminal sentinal
) {

    public ProductionRule getRule(int index) {
        return productionRules.get(index);
    }

    public GrammarParts getParts() {
        return new GrammarParts(
            tokens, 
            nonTerminals, 
            Set.copyOf(productionRules), 
            sentinal
        );
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof Grammar)) { return false; }

        Grammar otherGrammar = (Grammar)object;

        if(!sentinal.equals(otherGrammar.sentinal)) { return false; }

        if(!tokens.equals(otherGrammar.tokens)) { return false; }
        if(!nonTerminals.equals(otherGrammar.nonTerminals)) { return false; }

        if(!productionRules.equals(otherGrammar.productionRules)) { return false; }

        return true;
    }

    @Override
    public String toString() {
        String out = "";

        out += "Sentinal:\n\t" + sentinal.toString() + "\n\n";
        out += "Tokens:\n" + tabFormat(tokens) + "\n";
        out += "Non-terminals:\n" + tabFormat(nonTerminals) + "\n";
        out += "Production Rules:\n" + tabFormat(productionRules) + "\n";

        return out;
    }

    private <E> String tabFormat(Collection<E> collection) {
        String out = "";

        for (Object object : collection) {
            out += "\t" + object.toString() + "\n";
        }

        return out;
    }
}
