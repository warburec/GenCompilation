package syntax_analysis;

import java.util.*;

import grammar_objects.GrammarStructure;
import grammar_objects.fundamentals.*;

public abstract class SyntaxAnalyser {
    
    protected final Set<Token> tokens;
    protected final Set<NonTerminal> nonTerminals;
    protected final NonTerminal sentinel;
    protected final Set<ProductionRule> productionRules;

    protected Map<NonTerminal, Set<ProductionRule>> productionsForNonTerminal;
    
    public SyntaxAnalyser(Set<Token> tokens, Set<NonTerminal> nonTerminals, Set<ProductionRule> productionRules, NonTerminal sentinel) {
        this.tokens = tokens;
        this.nonTerminals = nonTerminals;
        this.productionRules = productionRules;
        this.sentinel = sentinel;
    }

    public SyntaxAnalyser(Token[] tokens, NonTerminal[] nonTerminals, ProductionRule[] productionRules, NonTerminal sentinel) {
        this.tokens = Set.of(tokens);
        this.nonTerminals = Set.of(nonTerminals);
        this.productionRules = Set.of(productionRules);
        this.sentinel = sentinel;
    }
    
    public abstract <T extends GrammarStructure> T analyse(T grammarStructure);
}