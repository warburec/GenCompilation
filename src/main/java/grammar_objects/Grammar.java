package grammar_objects;

import static helper_objects.ToStringFormatting.indentFormat;

import java.util.*;

public class Grammar {

    protected Set<Token> tokens;
    protected Set<NonTerminal> nonTerminals;
    protected List<ProductionRule> productionRules;
    protected NonTerminal sentinal;

    public Grammar(
        Set<Token> tokens,
        Set<NonTerminal> nonTerminals,
        List<ProductionRule> productionRules,
        NonTerminal sentinal
    ) {
        this.tokens = tokens;
        this.nonTerminals = nonTerminals;
        this.productionRules = productionRules;
        this.sentinal = sentinal;
    }

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
    public boolean equals(Object object) {
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
        out += "Tokens:\n" + indentFormat(tokens) + "\n\n";
        out += "Non-terminals:\n" + indentFormat(nonTerminals) + "\n\n";
        out += "Production Rules:\n" + indentFormat(productionRules) + "\n";

        return out;
    }

    public Set<Token> getTokens() {
        return tokens;
    }

    public Set<NonTerminal> getNonTerminals() {
        return nonTerminals;
    }

    public List<ProductionRule> getProductionRules() {
        return productionRules;
    }

    public NonTerminal getSentinal() {
        return sentinal;
    }
    
}
