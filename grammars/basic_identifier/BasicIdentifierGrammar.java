package grammars.basic_identifier;

import java.util.*;

import grammar_objects.*;

/**
 * <statement list> := <statement> |
 *                     <statement list> <statement>
 * <statement> := identifier = <element> + <element>;
 * <element> := identifier | number
 */
public class BasicIdentifierGrammar extends Grammar {

    @Override
    protected void setUpTokens(List<Token> tokens) {
        tokens.add(new Token("="));
        tokens.add(new Token("+"));
        tokens.add(new Token(";"));
        tokens.add(new Identifier("identifier"));
        tokens.add(new Literal("number"));
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("statement list");
    }

    @Override
    protected void setUpNonTerminals(List<NonTerminal> nonTerminals) {
        nonTerminals.add(new NonTerminal("statement list"));
        nonTerminals.add(new NonTerminal("statement"));
        nonTerminals.add(new NonTerminal("element"));
    }

    @Override
    protected void setUpProductionRules(List<ProductionRule> productionRules) {
        productionRules.add(new ProductionRule(
            new NonTerminal("statement list"),
            new LexicalElement[] {
                new NonTerminal("statement")
            }));
        
        productionRules.add(new ProductionRule(
            new NonTerminal("statement list"),
            new LexicalElement[] {
                new NonTerminal("statement list"),
                new NonTerminal("statement")
            }));
        
        productionRules.add(new ProductionRule(
            new NonTerminal("statement"),
            new LexicalElement[] {
                new Identifier("identifier"),
                new Token("="),
                new NonTerminal("element"),
                new Token("+"),
                new NonTerminal("element"),
                new Token(";")
            }));

        productionRules.add(new ProductionRule(
            new NonTerminal("element"),
            new LexicalElement[] {
                new Identifier("identifier")
            }));
        
        productionRules.add(new ProductionRule(
            new NonTerminal("element"),
            new LexicalElement[] {
                new Literal("number")
            }));
    }

}