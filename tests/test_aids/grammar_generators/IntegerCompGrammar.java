package tests.test_aids.grammar_generators;

import java.util.*;

import code_generation.BasicCodeGenerator;
import code_generation.Generator;
import code_generation.IdentifierGeneration;
import grammar_objects.*;
import semantic_analysis.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.ParseState;

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
public class IntegerCompGrammar extends LR0TestGrammar {

    Map<String, Map<String, Map<ProductionRule, Generator>>> semanticRuleConvertorMap = new HashMap<>();

    public IntegerCompGrammar() {
        super();

        setUpSemanticRuleConvertors(semanticRuleConvertorMap);
    }

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

    @Override
    protected void setUpStates(List<State> states, ProductionRule extraRootRule) {

    }

    @Override
    protected void setUpActionTable(Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
    }

    @Override
    protected void setUpGotoTable(Map<State, Map<NonTerminal, State>> gotoTable) {
    }

    @Override
    public ParseState getParseRoot(String sentence) {
        throw new UnsupportedOperationException("Unimplemented method 'getParseRoot'");
    }

    @Override
    protected void setUpGenerationBookends(Map<String, Map<String, String[]>> generationBookendMap) {
        generationBookendMap.put("Java", new HashMap<>());
        generationBookendMap.get("Java").put("JavaConversion", new String[] {
            "public class TestGrammar {\n" +
            "    public static void main(String[] args) {\n",

            "        System.out.println(x);\n" +
            "    }\n" +
            "}"
        });
    }

    @Override
    protected void setUpRuleConvertors(Map<String, Map<String, Map<ProductionRule, Generator>>> ruleConvertorMap) {

    }

    protected void setUpSemanticRuleConvertors(Map<String, Map<String, Map<ProductionRule, Generator>>> ruleConvertorMap) {
        ruleConvertorMap.put("Java", new HashMap<>());

        TypeChecker typeChecker = new TypeChecker();

        HashMap<ProductionRule, Generator> ruleConvertor = new HashMap<>();
        ruleConvertor.put(BasicCodeGenerator.ROOT_RULE, (elements) -> { return "        " + elements[0].getGeneration().stripTrailing().replaceAll("\n", "\n        ") + "\n"; });
        ruleConvertor.put(getRule(0), (elements) -> { return elements[0].getGeneration() + elements[1].getGeneration() + "\n"; }); //<statement list> := <statement list> <statement>
        ruleConvertor.put(getRule(1), (elements) -> { return elements[0].getGeneration() + "\n"; }); //<statement list> := <statement>
        ruleConvertor.put(getRule(2), (elements) -> { return elements[0].getGeneration() + ";"; }); //<statement> := <assignment>;
        ruleConvertor.put(getRule(3), (elements) -> { return elements[0].getGeneration() + "\n"; }); //<statement> := <if statement>
        ruleConvertor.put(getRule(4), (elements) -> { 
            String generation =  "\nif(" + elements[2].getGeneration() + ") {\n    ";
            generation += elements[5].getGeneration().stripTrailing().replaceAll("\n", "\n    ");
            return generation + "\n}";
        }); //<if statement> := if(<condition>) {<statement list>}
        ruleConvertor.put(getRule(5), (elements) -> {
            String generation =  elements[0].getGeneration() + "\nelse {\n    ";
            generation += elements[3].getGeneration().stripTrailing().replaceAll("\n", "\n    ");
            return generation + "\n}";
        }); //<if statement> := <if statement> else {<statement list>}
        ruleConvertor.put(getRule(6), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //<condition> := identifier <conditional operator> identifier
        ruleConvertor.put(getRule(7), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //<condition> := identifier <conditional operator> numConstant
        ruleConvertor.put(getRule(8), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := >
        ruleConvertor.put(getRule(9), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := >=
        ruleConvertor.put(getRule(10), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := ==
        ruleConvertor.put(getRule(11), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := !=
        ruleConvertor.put(getRule(12), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := <=
        ruleConvertor.put(getRule(13), (elements) -> { 
            String generation = "";

            IdentifierGeneration identifier = (IdentifierGeneration)elements[0];
            if(!typeChecker.isDeclared(identifier)) {
                typeChecker.declare(identifier);
                generation += identifier.getType() + " ";
            }
            
            generation += elements[0].getGeneration();
            return generation + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); 
        }); //<assignment> := identifier = <expression>
        ruleConvertor.put(getRule(14), (elements) -> { return elements[0].getGeneration(); }); // <expression> := val identifier
        ruleConvertor.put(getRule(15), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> + identifier
        ruleConvertor.put(getRule(16), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> - identifier
        ruleConvertor.put(getRule(17), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> * identifier
        ruleConvertor.put(getRule(18), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> / identifier
        ruleConvertor.put(getRule(19), (elements) -> { return elements[0].getGeneration(); }); // <expression> := numConstant
        ruleConvertor.put(getRule(20), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> + numConstant
        ruleConvertor.put(getRule(21), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> - numConstant
        ruleConvertor.put(getRule(22), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> * numConstant
        ruleConvertor.put(getRule(23), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> / numConstant
        ruleConvertorMap.get("Java").put("JavaConversion", ruleConvertor);
    }
    
    public Map<ProductionRule, Generator> getSemanticRuleConvertor(String sentence, String language) {
        Map<ProductionRule, Generator> ruleConvertor = null;

        try {
            ruleConvertor = semanticRuleConvertorMap.get(language).get(sentence);
        }
        catch (NullPointerException e) {
            throw new UnsupportedSentenceException("langage and rule convertor", sentence);
        }

        if(ruleConvertor == null) {
            throw new UnsupportedSentenceException("rule convertor", sentence);
        }

        return ruleConvertor;
    }

    @Override
    protected void setUpCodeGenerations(Map<String, Map<String, String>> codeGenerations) {

    }

}
