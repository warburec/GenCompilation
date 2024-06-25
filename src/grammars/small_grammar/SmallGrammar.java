package grammars.small_grammar;

import grammar_objects.*;

/**
 * E->E+B
 * E->E*B
 * E->B
 * B->0
 * B->1
 */
public class SmallGrammar extends Grammar {

    @Override
    protected  void setUpTokens(TokenOrganiser tokenOrganiser) {
        tokenOrganiser
        .addToken("0")
        .addToken("1")
        .addToken("*")
        .addToken("+");
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("E");
    }

    @Override
    protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
        nonTerminalOrganiser
        .addNonTerminal("E")
        .addNonTerminal("B");
    }

    @Override
    protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
        ruleOrganiser

        .addRule(
            new NonTerminal("E"),
            new LexicalElement[] {
                new NonTerminal("E"),
                new Token("+"),
                new NonTerminal("B")
        })

        .addRule(
            new NonTerminal("E"),
            new LexicalElement[] {
                new NonTerminal("E"),
                new Token("*"),
                new NonTerminal("B")
        })

        .addRule(
            new NonTerminal("E"),
            new LexicalElement[] {
                new NonTerminal("B")
        })

        .addRule(
            new NonTerminal("B"),
            new LexicalElement[] {
                new Token("0")
        })

        .addRule(
            new NonTerminal("B"),
            new LexicalElement[] {
                new Token("1")
        });
    }

}
