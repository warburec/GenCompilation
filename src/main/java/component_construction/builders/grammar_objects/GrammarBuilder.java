package component_construction.builders.grammar_objects;

import java.util.*;
import grammar_objects.*;

public class GrammarBuilder {

    protected Set<Token> tokens = new HashSet<>();
    protected Set<NonTerminal> nonTerminals = new HashSet<>();
    protected List<ProductionRule> productionRules = new ArrayList<>();
    protected NonTerminal sentinal;

    /**
     * Sets up the sentinal for the grammar being built
     * @param sentinal The sentinal non-terminal
     * @return This builder for method chaining
     */
    public GrammarBuilder setSentinal(NonTerminal sentinal) {
        this.sentinal = sentinal;
        return this;
    }

    /**
     * Adds a production rule for the grammar being built
     * @param productionRule The production rule
     * @return This builder for method chaining
     */
    public GrammarBuilder addRule(ProductionRule productionRule) {
        addTokens(productionRule);
        productionRules.add(productionRule);
        return this;
    }

    /**
     * Adds a production rule for the grammar being built
     * @param nonTerminal The NonTerminal for the rule
     * @param elements In-order elements for the rule
     * @return This builder for method chaining
     */
    public GrammarBuilder addRule(NonTerminal nonTerminal, LexicalElement[] elements) {
        return addRule(new ProductionRule(nonTerminal, elements));
    }

    /**
     * Adds a production rule for the grammar being built
     * @param nonTerminalName The name of the NonTerminal for this rule
     * @param elements In-order elements for this rule
     * @return This builder for method chaining
     */
    public GrammarBuilder addRule(String nonTerminalName, LexicalElement[] elements) {
        return addRule(new NonTerminal(nonTerminalName), elements);
    }

    /**
     * Adds a collection of production rules to the grammar being built
     * @param rules The collection of rules
     * @return This builder for method chaining
     */
    public GrammarBuilder addRules(Collection<ProductionRule> rules) {
        for (ProductionRule productionRule : rules) {
            addRule(productionRule);
        }

        return this;
    }

    /**
     * Produces the grammar built through this builder
     * @return The grammar that was built
     */
    public Grammar produceGrammar() {
        if (sentinal == null) throw new MissingSentinalException();
        return new Grammar(tokens, nonTerminals, productionRules, sentinal);
    }


    private void addTokens(ProductionRule productionRule) {
        nonTerminals.add(productionRule.nonTerminal());

        for (LexicalElement element : productionRule.productionSequence()) {
            if (element instanceof Token) { tokens.add((Token)element); }
            else if (element instanceof NonTerminal) { nonTerminals.add((NonTerminal)element); }
        }
    }


    public class MissingSentinalException extends RuntimeException {
        
        public MissingSentinalException() {
            super("Sentinal not defined");
        }

    }

}
