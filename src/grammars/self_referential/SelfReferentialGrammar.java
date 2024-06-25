package grammars.self_referential;

import grammar_objects.*;

/**
 * H → h A
 * A → a L
 * L → l L //Self-referential
 * L → o
 */
public class SelfReferentialGrammar extends Grammar {

    @Override
    protected void setUpTokens(TokenOrganiser tokenOrganiser) {
        tokenOrganiser
        .addToken("h")
        .addToken("a")
        .addToken("l")
        .addToken("o");
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("H");
    }

    @Override
    protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
        nonTerminalOrganiser
        .addNonTerminal("H")
        .addNonTerminal("A")
        .addNonTerminal("L");
    }

    @Override
    protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
        ruleOrganiser

        .addRule(
            new NonTerminal("H"),
            new LexicalElement[] {
                new Token("h"),
                new NonTerminal("A")
        })
        
        .addRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("a"),
                new NonTerminal("L")
        })
        
        .addRule(
            new NonTerminal("L"),
            new LexicalElement[] {
                new Token("l"),
                new NonTerminal("L")
        })
        
        .addRule(
            new NonTerminal("L"),
            new LexicalElement[] {
                new Token("o")
        });
    }

}
