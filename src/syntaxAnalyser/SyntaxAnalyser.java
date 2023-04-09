package syntaxAnalyser;

import java.util.Map;

import GrammarObjects.*;

public abstract class SyntaxAnalyser {
    
    private Token[] tokens;
    private NonTerminal[] nonTerminals;
    private ProductionRule[] productionRules;
    private Map<NonTerminal, ProductionRule[]> productionsForNonTerminal;

    SyntaxAnalyser(Token[] tokens, NonTerminal[] nonTerminals, ProductionRule[] productionRules) {
        this.tokens = tokens;
        this.nonTerminals = nonTerminals;
        this.productionRules = productionRules;
    }
    
}