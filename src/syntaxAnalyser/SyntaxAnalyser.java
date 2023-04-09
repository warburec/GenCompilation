package syntaxAnalyser;

import java.util.Map;

import GrammarObjects.*;

public abstract class SyntaxAnalyser {
    
    protected final Token[] tokens;
    protected final NonTerminal[] nonTerminals;
    protected final NonTerminal sentinel;
    protected final ProductionRule[] productionRules;
    
    protected Map<NonTerminal, ProductionRule[]> productionsForNonTerminal;
    
    SyntaxAnalyser(Token[] tokens, NonTerminal[] nonTerminals, ProductionRule[] productionRules, NonTerminal sentinel) {
        this.tokens = tokens;
        this.nonTerminals = nonTerminals;
        this.productionRules = productionRules;
        this.sentinel = sentinel;
    }
    
    public abstract <T extends GrammarStructure> T analyse(T grammarStructure);
}