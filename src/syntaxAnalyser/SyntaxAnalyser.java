package syntaxAnalyser;

import java.util.List;
import java.util.Map;

import GrammarObjects.*;

public abstract class SyntaxAnalyser {
    
    protected final List<Token> tokens;
    protected final List<NonTerminal> nonTerminals;
    protected final NonTerminal sentinel;
    protected final List<ProductionRule> productionRules;

    protected Map<NonTerminal, ProductionRule[]> productionsForNonTerminal;
    
    SyntaxAnalyser(List<Token> tokens, List<NonTerminal> nonTerminals, List<ProductionRule> productionRules, NonTerminal sentinel) {
        this.tokens = tokens;
        this.nonTerminals = nonTerminals;
        this.productionRules = productionRules;
        this.sentinel = sentinel;
    }
    
    public abstract <T extends GrammarStructure> T analyse(T grammarStructure);
}