package grammar_objects;

import java.util.*;

public abstract class Grammar {
    
    protected List<Token> tokens = new ArrayList<>();
    protected List<NonTerminal> nonTerminals = new ArrayList<>();
    protected List<ProductionRule> productionRules = new ArrayList<>();
    protected NonTerminal sentinal;
    
    public Grammar() {
        setUpTokens(new TokenOrganiser());
        sentinal = setUpSentinal();
        setUpNonTerminals(new NonTerminalOrganiser());
        setUpProductionRules(new RuleOrganiser());
    }

    protected abstract void setUpTokens(TokenOrganiser tokenOrganiser);
    protected abstract NonTerminal setUpSentinal();
    protected abstract void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser);
    protected abstract void setUpProductionRules(RuleOrganiser ruleOrganiser);

    public ProductionRule getRule(int index) {
        return productionRules.get(index);
    }

    public GrammarParts getParts() {
        return new GrammarParts(
            Set.copyOf(tokens), 
            Set.copyOf(nonTerminals), 
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
}
