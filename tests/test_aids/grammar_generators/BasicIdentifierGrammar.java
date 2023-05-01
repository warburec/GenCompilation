package tests.test_aids.grammar_generators;

import java.util.*;

import code_generation.*;
import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;

/**
 * <statement list> := <statement> |
 *                     <statement list> <statement>
 * <statement> := identifier = <element> + <element>;
 * <element> := identifier | number
 */
public class BasicIdentifierGrammar extends TestGrammar {

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

    @Override
    protected void setUpStates(List<State> states, ProductionRule extraRootRule) {
        states.add(new State( //0
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 0),
                new GrammarPosition(getRule(1), 0),
                new GrammarPosition(getRule(0), 0),
                new GrammarPosition(getRule(2), 0)
            }),
            null
        ));

        states.add(new State( //1
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 1),
                new GrammarPosition(getRule(1), 1),
                new GrammarPosition(getRule(2), 0)
            }),
            getState(0)
        ));

        states.add(new State( //2
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 2),
            }),
            getState(1)
        ));

        states.add(new State( //3
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 1),
            }),
            getState(1)
        ));

        states.add(new State( //4
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 2),
                new GrammarPosition(getRule(3), 0),
                new GrammarPosition(getRule(4), 0)
            }),
            getState(3)
        ));

        states.add(new State( //5
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 3)
            }),
            getState(4)
        ));

        states.add(new State( //6
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 4),
                new GrammarPosition(getRule(3), 0),
                new GrammarPosition(getRule(4), 0)
            }),
            getState(5)
        ));

        states.add(new State( //7
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 5)
            }),
            getState(6)
        ));

        states.add(new State( //8
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(3), 1)
            }),
            getState(6)
        ));

        states.add(new State( //9
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(4), 1)
            }),
            getState(6)
        ));

        states.add(new State( //10
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 1)
            }),
            getState(0)
        ));

        states.add(new State( //11
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 6)
            }),
            getState(7)
        ));

        //Branches
        getState(0)
            .addBranch(new Route(getState(1), new NonTerminal("statement list")))
            .addBranch(new Route(getState(10), new NonTerminal("statement")))
            .addBranch(new Route(getState(3), new Identifier("identifier")));
        
        getState(1)
            .addBranch(new Route(getState(2), new NonTerminal("statement")))
            .addBranch(new Route(getState(3), new Identifier("identifier")));
        
        getState(3)
            .addBranch(new Route(getState(4), new Token("=")));
        
        getState(4)
            .addBranch(new Route(getState(5), new NonTerminal("element")))
            .addBranch(new Route(getState(8), new Identifier("identifier")))
            .addBranch(new Route(getState(9), new Literal("number")));
        
        getState(5)
            .addBranch(new Route(getState(6), new Token("+")));
        
        getState(6)
            .addBranch(new Route(getState(7), new NonTerminal("element")))
            .addBranch(new Route(getState(8), new Identifier("identifier")))
            .addBranch(new Route(getState(9), new Literal("number")));
        
        getState(7)
            .addBranch(new Route(getState(11), new Token(";")));
    }

    @Override
    protected void setUpActionTable(Map<State, Action> actionTable) {
        Map<Token, State> currentStateActions = new HashMap<>();

        currentStateActions.put(new Identifier("identifier"), getState(3));
        actionTable.put(getState(0), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        currentStateActions.put(new Identifier("identifier"), getState(3));
        actionTable.put(getState(1), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        actionTable.put(getState(2), new ReduceAction(getRule(1)));

        currentStateActions.put(new Token("="), getState(4));
        actionTable.put(getState(3), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        currentStateActions.put(new Identifier("identifier"), getState(8));
        currentStateActions.put(new Literal("number"), getState(9));
        actionTable.put(getState(4), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        currentStateActions.put(new Token("+"), getState(6));
        actionTable.put(getState(5), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        currentStateActions.put(new Identifier("identifier"), getState(8));
        currentStateActions.put(new Literal("number"), getState(9));
        actionTable.put(getState(6), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        currentStateActions.put(new Token(";"), getState(11));
        actionTable.put(getState(7), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        actionTable.put(getState(8), new ReduceAction(getRule(3)));

        actionTable.put(getState(9), new ReduceAction(getRule(4)));

        actionTable.put(getState(10), new ReduceAction(getRule(0)));

        actionTable.put(getState(11), new ReduceAction(getRule(2)));
    }

    @Override
    protected void setUpGotoTable(Map<State, Map<NonTerminal, State>> gotoTable) {
        Map<NonTerminal, State> currentGotoActions = new HashMap<>();

        currentGotoActions.put(new NonTerminal("statement list"), getState(1));
        currentGotoActions.put(new NonTerminal("statement"), getState(10));
        gotoTable.put(getState(0), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("statement"), getState(2));
        gotoTable.put(getState(1), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("element"), getState(5));
        gotoTable.put(getState(4), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("element"), getState(7));
        gotoTable.put(getState(6), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();
    }


    @Override
    public ParseState getParseRoot(String sentence) {
        switch(sentence) {
            case "XToYToX":
                return parseTree0();
            
            default:
                throw new UnsupportedSentenceException("parse tree", sentence);
        }
    }

    /**
     * Parse tree for the sentence "XToYToX"
     * @return The root ParseState of the tree
     */
    private ParseState parseTree0() {
        List<ParseState> parseStates = new ArrayList<>();

        parseStates.add(new ShiftedState(
            getState(3), 
            new Identifier("identifier", "int", "x")));

        parseStates.add(new ShiftedState(
            getState(4), 
            new Token("=")));
        
        parseStates.add(new ShiftedState(
            getState(9), 
            new Literal("number", "1")));
        
        parseStates.add(new ShiftedState(
            getState(6), 
            new Token("+")));
        
        parseStates.add(new ShiftedState(
            getState(9), 
            new Literal("number", "2")));
        
        parseStates.add(new ShiftedState(
            getState(11), 
            new Token(";")));
        
        parseStates.add(new ShiftedState(
            getState(3), 
            new Identifier("identifier", "int", "y")));
        
        parseStates.add(new ShiftedState(
            getState(4), 
            new Token("=")));
        
        parseStates.add(new ShiftedState(
            getState(8), 
            new Identifier("identifier", "int", "x")));
        
        parseStates.add(new ShiftedState(
            getState(6), 
            new Token("+")));
        
        parseStates.add(new ShiftedState(
            getState(9), 
            new Literal("number", "3")));
        
        parseStates.add(new ShiftedState(
            getState(11), 
            new Token(";")));
        
        parseStates.add(new ShiftedState(
            getState(3), 
            new Identifier("identifier", "int", "x")));
        
        parseStates.add(new ShiftedState(
            getState(4), 
            new Token("=")));
        
        parseStates.add(new ShiftedState(
            getState(8), 
            new Identifier("identifier", "int", "y")));
        
        parseStates.add(new ShiftedState(
            getState(6), 
            new Token("+")));
        
        parseStates.add(new ShiftedState(
            getState(9), 
            new Literal("number", "0")));
        
        parseStates.add(new ShiftedState(
            getState(11), 
            new Token(";")));
            

        parseStates.add(new ReducedState(
            getState(5), 
            getRule(4), 
            Arrays.asList(new ParseState[] {
                parseStates.get(2)
            })));
        
        parseStates.add(new ReducedState(
            getState(7), 
            getRule(4), 
            Arrays.asList(new ParseState[] {
                parseStates.get(4)
            })));
        
        parseStates.add(new ReducedState(
            getState(5), 
            getRule(3), 
            Arrays.asList(new ParseState[] {
                parseStates.get(8)
            })));
        
        parseStates.add(new ReducedState(
            getState(7), 
            getRule(4),
            Arrays.asList(new ParseState[] {
                parseStates.get(10)
            })));
        
        parseStates.add(new ReducedState(
            getState(5), 
            getRule(3), 
            Arrays.asList(new ParseState[] {
                parseStates.get(14)
            })));
        
        parseStates.add(new ReducedState(
            getState(7), 
            getRule(4),
            Arrays.asList(new ParseState[] {
                parseStates.get(16)
            })));

        
        parseStates.add(new ReducedState(
            getState(10), 
            getRule(2), 
            Arrays.asList(new ParseState[] {
                parseStates.get(0),
                parseStates.get(1),
                parseStates.get(18),
                parseStates.get(3),
                parseStates.get(19),
                parseStates.get(5)
            })));
        
        parseStates.add(new ReducedState(
            getState(2), 
            getRule(2), 
            Arrays.asList(new ParseState[] {
                parseStates.get(6),
                parseStates.get(7),
                parseStates.get(20),
                parseStates.get(9),
                parseStates.get(21),
                parseStates.get(11)
            })));
        
        parseStates.add(new ReducedState(
            getState(2), 
            getRule(2), 
            Arrays.asList(new ParseState[] {
                parseStates.get(12),
                parseStates.get(13),
                parseStates.get(22),
                parseStates.get(15),
                parseStates.get(23),
                parseStates.get(17)
            })));
        
        
        parseStates.add(new ReducedState(
            getState(1), 
            getRule(0), 
            Arrays.asList(new ParseState[] {
                parseStates.get(24)
            })));

        
        parseStates.add(new ReducedState(
            getState(1), 
            getRule(1), 
            Arrays.asList(new ParseState[] {
                parseStates.get(27),
                parseStates.get(25)
            })));
        
        parseStates.add(new ReducedState(
            getState(1), 
            getRule(1), 
            Arrays.asList(new ParseState[] {
                parseStates.get(28),
                parseStates.get(26)
            })));
        
        return parseStates.get(parseStates.size() - 1);
    }


    @Override
    protected void setUpGenerationBookends(Map<String, Map<String, String[]>> generationBookendMap) {
        generationBookendMap.put("Java", new HashMap<>());
        generationBookendMap.get("Java").put("XToYToX", new String[] {
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n",

            "\t\tSystem.out.println(x);\n" +
            "\t}\n" +
            "}"
        });
    }

    @Override
    protected void setUpRuleConvertors(Map<String, Map<String, Map<ProductionRule, Generator>>> ruleConvertorMap) {
        ruleConvertorMap.put("Java", new HashMap<>());

        HashMap<ProductionRule, Generator> ruleConvertor = new HashMap<>();
        ruleConvertor.put(getRule(0), (generator, elements) -> { return elements[0].getGeneration(); }); //<statement list> := <statement>
        ruleConvertor.put(getRule(1), (generator, elements) -> { return elements[0].getGeneration() + elements[1].getGeneration(); }); //<statement list> := <statement list> <statement>
        ruleConvertor.put(getRule(2), (generator, elements) -> {
            String identifierType = "";
            IdentifierGeneration identifier = (IdentifierGeneration)elements[0];
            if(!generator.isDeclared(identifier)) {
                identifierType = identifier.getType() + " ";
                generator.setDeclared(identifier);
            }

            return "\t\t" + identifierType + elements[0].getGeneration() + " " + 
            elements[1].getGeneration() + " " + elements[2].getGeneration() + " " + 
            elements[3].getGeneration() + " " + elements[4].getGeneration() + 
            elements[5].getGeneration() + "\n"; 
        });  //<statement> := identifier = <element> + <element>;
        ruleConvertor.put(getRule(3), (generator, elements) -> { return ((IdentifierGeneration)elements[0]).getGeneration(); }); //<element> := identifier
        ruleConvertor.put(getRule(4), (generator, elements) -> { return ((LiteralGeneration)elements[0]).getGeneration(); }); //<element> := number
        ruleConvertorMap.get("Java").put("XToYToX", ruleConvertor);
    }

    @Override
    protected void setUpCodeGenerations(Map<String, Map<String, String>> codeGenerations) {
        codeGenerations.put("Java", new HashMap<>());
        codeGenerations.get("Java").put("XToYToX",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tint x = 1 + 2;\n" +
            "\t\tint y = x + 3;\n" +
            "\t\tx = y + 0;\n" +
            "\t\tSystem.out.println(x);\n" +
            "\t}\n" +
            "}"
        );
    }

}