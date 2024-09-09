package grammars.integer_comparison;

import grammar_objects.*;

// statement_list -> statement_list statement
// statement_list -> statement
// statement -> assignment ;
// statement -> if_statement
// if_statement -> if ( condition ) { statement_list }
// if_statement -> if_statement else { statement_list }
// condition -> identifier conditional_operator identifier
// condition -> identifier conditional_operator numConstant
// conditional_operator -> >
// conditional_operator -> >=
// conditional_operator -> ==
// conditional_operator -> !=
// conditional_operator -> <=
// assignment -> identifier = expression
// expression -> identifier
// expression -> expression + identifier
// expression -> expression - identifier
// expression -> expression * identifier
// expression -> expression / identifier
// expression -> numConstant
// expression -> expression + numConstant
// expression -> expression - numConstant
// expression -> expression * numConstant
// expression -> expression / numConstant
public class IntegerCompGrammar extends Grammar {

    @Override
    protected void setUpTokens(TokenOrganiser tokenOrganiser) {
        tokenOrganiser
        .addToken(new Token(";"))
        .addToken(new Token("if"))
        .addToken(new Token("("))
        .addToken(new Token(")"))
        .addToken(new Token("{"))
        .addToken(new Token("}"))
        .addToken(new Token("else"))
        .addToken(new Token(">"))
        .addToken(new Token(">="))
        .addToken(new Token("=="))
        .addToken(new Token("!="))
        .addToken(new Token("<="))
        .addToken(new Token("="))
        .addToken(new Token("+"))
        .addToken(new Token("-"))
        .addToken(new Token("*"))
        .addToken(new Token("/"))
        .addToken(new Identifier("identifier"))
        .addToken(new Literal("numConstant"));
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
        .addNonTerminal("if statement")
        .addNonTerminal("condition")
        .addNonTerminal("conditional operator")
        .addNonTerminal("assignment")
        .addNonTerminal("expression");
    }

    @Override
    protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
        ruleOrganiser

        .addRule(
            new NonTerminal("statement list"), 
            new LexicalElement[] {
                new NonTerminal("statement list"),
                new NonTerminal("statement")
        })
        
        .addRule(
            new NonTerminal("statement list"), 
            new LexicalElement[] {
                new NonTerminal("statement")
        })
        
        .addRule(
            new NonTerminal("statement"), 
            new LexicalElement[] {
                new NonTerminal("assignment"),
                new Token(";")
        })
        
        .addRule(
            new NonTerminal("statement"), 
            new LexicalElement[] {
                new NonTerminal("if statement")
        })

        .addRule(
            new NonTerminal("if statement"), 
            new LexicalElement[] {
                new Token("if"),
                new Token("("),
                new NonTerminal("condition"),
                new Token(")"),
                new Token("{"),
                new NonTerminal("statement list"),
                new Token("}")
        })

        .addRule(
            new NonTerminal("if statement"), 
            new LexicalElement[] {
                new NonTerminal("if statement"),
                new Token("else"),
                new Token("{"),
                new NonTerminal("statement list"),
                new Token("}")
        })

        .addRule(
            new NonTerminal("condition"), 
            new LexicalElement[] {
                new Identifier("identifier"),
                new NonTerminal("conditional operator"),
                new Identifier("identifier")
        })

        .addRule(
            new NonTerminal("condition"), 
            new LexicalElement[] {
                new Identifier("identifier"),
                new NonTerminal("conditional operator"),
                new Literal("numConstant")
        })

        .addRule(
            new NonTerminal("conditional operator"), 
            new LexicalElement[] {
                new Token(">")
        })

        .addRule(
            new NonTerminal("conditional operator"), 
            new LexicalElement[] {
                new Token(">=")
        })

        .addRule(
            new NonTerminal("conditional operator"), 
            new LexicalElement[] {
                new Token("==")
        })

        .addRule(
            new NonTerminal("conditional operator"), 
            new LexicalElement[] {
                new Token("!=")
        })

        .addRule(
            new NonTerminal("conditional operator"), 
            new LexicalElement[] {
                new Token("<=")
        })

        .addRule(
            new NonTerminal("assignment"), 
            new LexicalElement[] {
                new Identifier("identifier"),
                new Token("="),
                new NonTerminal("expression")
        })

        .addRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new Identifier("identifier")
        })

        .addRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("+"),
                new Identifier("identifier")
        })

        .addRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("-"),
                new Identifier("identifier")
        })

        .addRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("*"),
                new Identifier("identifier")
        })

        .addRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("/"),
                new Identifier("identifier")
        })


        .addRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new Literal("numConstant")
        })

        .addRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("+"),
                new Literal("numConstant")
        })

        .addRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("-"),
                new Literal("numConstant")
        })

        .addRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("*"),
                new Literal("numConstant")
        })

        .addRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("/"),
                new Literal("numConstant")
        });
    }

}
