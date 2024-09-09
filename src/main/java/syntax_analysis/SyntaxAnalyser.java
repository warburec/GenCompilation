package syntax_analysis;

import java.util.*;

import grammar_objects.*;
import helperObjects.LexElementFinder;
import syntax_analysis.parsing.*;

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
    
    public SyntaxAnalyser(Set<ProductionRule> productionRules, NonTerminal sentinel) {
        this.tokens = LexElementFinder.tokens(productionRules);
        this.nonTerminals = LexElementFinder.nonTerminals(productionRules);
        this.productionRules = productionRules;
        this.sentinel = sentinel;
    }

    public SyntaxAnalyser(ProductionRule[] productionRules, NonTerminal sentinel) {
        this.tokens = LexElementFinder.tokens(productionRules);
        this.nonTerminals = LexElementFinder.nonTerminals(productionRules);
        this.productionRules = Set.of(productionRules);
        this.sentinel = sentinel;
    }

    public abstract ParseState analyse(Token[] inputTokens) throws ParseFailedException;
}