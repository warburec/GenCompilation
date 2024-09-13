package grammars.basic_CLR1;

import grammar_objects.*;

public class BasicCLR1Grammar extends Grammar {

    @Override
    protected void setUpTokens(TokenOrganiser tokenOrganiser) {
        tokenOrganiser
        .addToken("a")
        .addToken("b");
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("S");
    }

    @Override
    protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
        nonTerminalOrganiser
        .addNonTerminal("S")
        .addNonTerminal("X");
    }

    @Override
    protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
        ruleOrganiser

        .addRule(
            new NonTerminal("S"), 
            new LexicalElement[] {
                new NonTerminal("X"),
                new NonTerminal("X")
        })

        .addRule(
            new NonTerminal("X"),
            new LexicalElement[] {
                new Token("a"),
                new NonTerminal("X")
        })

        .addRule(
            new NonTerminal("X"),
            new LexicalElement[] {
                new Token("b")
        });
    }
}
