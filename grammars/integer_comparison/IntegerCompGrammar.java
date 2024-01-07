package grammars.integer_comparison;

import java.util.*;

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
    protected void setUpTokens(List<Token> tokens) {
        tokens.add(new Token(";"));
        tokens.add(new Token("if"));
        tokens.add(new Token("("));
        tokens.add(new Token(")"));
        tokens.add(new Token("{"));
        tokens.add(new Token("}"));
        tokens.add(new Token("else"));
        tokens.add(new Token(">"));
        tokens.add(new Token(">="));
        tokens.add(new Token("=="));
        tokens.add(new Token("!="));
        tokens.add(new Token("<="));
        tokens.add(new Token("="));
        tokens.add(new Token("+"));
        tokens.add(new Token("-"));
        tokens.add(new Token("*"));
        tokens.add(new Token("/"));
        tokens.add(new Identifier("identifier"));
        tokens.add(new Literal("numConstant"));
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("statement list");
    }

    @Override
    protected void setUpNonTerminals(List<NonTerminal> nonTerminals) {
        nonTerminals.add(new NonTerminal("statement list"));
        nonTerminals.add(new NonTerminal("statement"));
        nonTerminals.add(new NonTerminal("if statement"));
        nonTerminals.add(new NonTerminal("condition"));
        nonTerminals.add(new NonTerminal("conditional operator"));
        nonTerminals.add(new NonTerminal("assignment"));
        nonTerminals.add(new NonTerminal("expression"));
    }

    @Override
    protected void setUpProductionRules(List<ProductionRule> productionRules) {
        productionRules.add(new ProductionRule(
            new NonTerminal("statement list"), 
            new LexicalElement[] {
                new NonTerminal("statement list"),
                new NonTerminal("statement")
        }));
        
        productionRules.add(new ProductionRule(
            new NonTerminal("statement list"), 
            new LexicalElement[] {
                new NonTerminal("statement")
        }));
        
        productionRules.add(new ProductionRule(
            new NonTerminal("statement"), 
            new LexicalElement[] {
                new NonTerminal("assignment"),
                new Token(";")
        }));
        
        productionRules.add(new ProductionRule(
            new NonTerminal("statement"), 
            new LexicalElement[] {
                new NonTerminal("if statement")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("if statement"), 
            new LexicalElement[] {
                new Token("if"),
                new Token("("),
                new NonTerminal("condition"),
                new Token(")"),
                new Token("{"),
                new NonTerminal("statement list"),
                new Token("}")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("if statement"), 
            new LexicalElement[] {
                new NonTerminal("if statement"),
                new Token("else"),
                new Token("{"),
                new NonTerminal("statement list"),
                new Token("}")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("condition"), 
            new LexicalElement[] {
                new Identifier("identifier"),
                new NonTerminal("conditional operator"),
                new Identifier("identifier")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("condition"), 
            new LexicalElement[] {
                new Identifier("identifier"),
                new NonTerminal("conditional operator"),
                new Literal("numConstant")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("conditional operator"), 
            new LexicalElement[] {
                new Token(">")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("conditional operator"), 
            new LexicalElement[] {
                new Token(">=")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("conditional operator"), 
            new LexicalElement[] {
                new Token("==")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("conditional operator"), 
            new LexicalElement[] {
                new Token("!=")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("conditional operator"), 
            new LexicalElement[] {
                new Token("<=")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("assignment"), 
            new LexicalElement[] {
                new Identifier("identifier"),
                new Token("="),
                new NonTerminal("expression")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new Identifier("identifier")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("+"),
                new Identifier("identifier")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("-"),
                new Identifier("identifier")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("*"),
                new Identifier("identifier")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("/"),
                new Identifier("identifier")
        }));


        productionRules.add(new ProductionRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new Literal("numConstant")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("+"),
                new Literal("numConstant")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("-"),
                new Literal("numConstant")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("*"),
                new Literal("numConstant")
        }));

        productionRules.add(new ProductionRule(
            new NonTerminal("expression"), 
            new LexicalElement[] {
                new NonTerminal("expression"),
                new Token("/"),
                new Literal("numConstant")
        }));
    }

}
