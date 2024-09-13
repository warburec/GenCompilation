package grammar_objects;

import java.util.*;

public abstract class Grammar {
    
    protected Set<Token> tokens = new HashSet<>();
    protected Set<NonTerminal> nonTerminals = new HashSet<>();
    protected List<ProductionRule> productionRules = new ArrayList<>();
    protected NonTerminal sentinal;
    
    public Grammar() {
        setUpTokens(new TokenOrganiser());
        sentinal = setUpSentinal();
        setUpNonTerminals(new NonTerminalOrganiser());
        setUpProductionRules(new RuleOrganiser());
    }

    /**
     * Sets up the tokens for this grammar.
     * @param tokenOrganiser An object which is passed to this method to store created tokens.
     */
    protected abstract void setUpTokens(TokenOrganiser tokenOrganiser);
    /**
     * Sets up the sentinal token for this grammar.
     * @return The sentinal token for this grammar.
     */
    protected abstract NonTerminal setUpSentinal();
    /**
     * Sets up the non-terminals for this grammar.
     * @param nonTerminalOrganiser An object which is passed to this method to store created non-terminals.
     */
    protected abstract void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser);
    /**
     * Sets up the production rules for this grammar.
     * @param ruleOrganiser An object which is passed to this method to store specified production rules.
     */
    protected abstract void setUpProductionRules(RuleOrganiser ruleOrganiser);

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


    protected class TokenOrganiser {
        public TokenOrganiser addToken(Token token) {
            tokens.add(token);
            return this;
        }

        /**
         * Adds a token for this Grammar. Note: The name will be converted to an object of type Token. Use addToken(Token) to include Token Subtypes.
         * @param tokenName The name of the Token to be added.
         * @return This TokenOrganiser, for method chaining.
         */
        public TokenOrganiser addToken(String tokenName) {
            return addToken(new Token(tokenName));
        }
    }

    protected class NonTerminalOrganiser {
        public NonTerminalOrganiser addNonTerminal(NonTerminal nonTerminal) {
            nonTerminals.add(nonTerminal);
            return this;
        }

        /**
         * Adds a NonTerminal for this Grammar. Note: The name will be converted to an object of type NonTerminal. Use addNonTerminal(NonTerminal) to include NonTerminal Subtypes.
         * @param nonTerminalName The name of the NonTerminal to be added.
         * @return This NonTerminalOrganiser, for method chaining.
         */
        public NonTerminalOrganiser addNonTerminal(String nonTerminalName) {
            return addNonTerminal(new NonTerminal(nonTerminalName));
        }
    }

    protected class RuleOrganiser {
        public RuleOrganiser addRule(ProductionRule productionRule) {
            productionRules.add(productionRule);
            return this;
        }

        /**
         * Adds a new ProductionRule for this Grammar.
         * @param nonTerminal The NonTerminal for this rule
         * @param elements The in-order elements for this rule
         * @return This RuleOrganiser, for method chaining.
         */
        public RuleOrganiser addRule(NonTerminal nonTerminal, LexicalElement[] elements) {
            return addRule(new ProductionRule(nonTerminal, elements));
        }

        /**
         * Adds a new ProductionRule for this Grammar.
         * @param nonTerminalName The name for the NonTerminal for this rule
         * @param elements The in-order elements for this rule
         * @return This RuleOrganiser, for method chaining.
         */
        public RuleOrganiser addRule(String nonTerminalName, LexicalElement[] elements) {
            return addRule(new NonTerminal(nonTerminalName), elements);
        }
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof Grammar)) { return false; }

        Grammar otherGrammar = (Grammar)object;

        if(!Set.copyOf(tokens).containsAll(otherGrammar.tokens)) { return false; }
        if(!sentinal.equals(otherGrammar.sentinal)) { return false; }
        if(!Set.copyOf(nonTerminals).containsAll(otherGrammar.nonTerminals)) { return false; }

        if(!Set.copyOf(productionRules).containsAll(otherGrammar.productionRules)) { return false; }

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
