package grammars.basic_SLR1;

import grammar_objects.*;

/**
 * S –> AA    
 * A –> aA
 * A –> b
 */
public class BasicSLR1Grammar extends Grammar {

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
        .addNonTerminal("A");
    }

    @Override
    protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
        ruleOrganiser

        .addRule(
            new NonTerminal("S"),
            new LexicalElement[] {
                new NonTerminal("A"),
                new NonTerminal("A")
            }
        )
        .addRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("a"),
                new NonTerminal("A")
            }
        )
        .addRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("b")
            }
        );
    }

}
