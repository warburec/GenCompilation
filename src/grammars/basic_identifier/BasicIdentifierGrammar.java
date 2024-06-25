package grammars.basic_identifier;

import grammar_objects.*;

/**
 * <statement list> := <statement> |
 *                     <statement list> <statement>
 * <statement> := identifier = <element> + <element>;
 * <element> := identifier | number
 */
public class BasicIdentifierGrammar extends Grammar {

    @Override
    protected void setUpTokens(TokenOrganiser tokenOrganiser) {
        tokenOrganiser
        .addToken(new Token("="))
        .addToken(new Token("+"))
        .addToken(new Token(";"))
        .addToken(new Identifier("identifier"))
        .addToken(new Literal("number"));
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("statement list");
    }

    @Override
    protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
        nonTerminalOrganiser
        .addNonTerminal("statement list")
        .addNonTerminal("statement")
        .addNonTerminal("element");
    }

    @Override
    protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
        ruleOrganiser

        .addRule(
            new NonTerminal("statement list"),
            new LexicalElement[] {
                new NonTerminal("statement")
        })
        
        .addRule(
            new NonTerminal("statement list"),
            new LexicalElement[] {
                new NonTerminal("statement list"),
                new NonTerminal("statement")
        })
        
        .addRule(
            new NonTerminal("statement"),
            new LexicalElement[] {
                new Identifier("identifier"),
                new Token("="),
                new NonTerminal("element"),
                new Token("+"),
                new NonTerminal("element"),
                new Token(";")
        })

        .addRule(
            new NonTerminal("element"),
            new LexicalElement[] {
                new Identifier("identifier")
        })
        
        .addRule(
            new NonTerminal("element"),
            new LexicalElement[] {
                new Literal("number")
        });
    }

}