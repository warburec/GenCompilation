package tests.test_aids.grammar_generators;

import java.util.*;

import code_generation.Generator;
import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;

/**
 * E->E+B
 * E->E*B
 * E->B
 * B->0
 * B->1
 */
public class SmallTestGrammar extends TestGrammar {

    @Override
    protected  void setUpTokens(List<Token> tokens) {
        tokens.add(new Token("0"));
        tokens.add(new Token("1"));
        tokens.add(new Token("*"));
        tokens.add(new Token("+"));
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("E");
    }

    @Override
    protected void setUpNonTerminals(List<NonTerminal> nonTerminals) {
        nonTerminals.add(new NonTerminal("E"));
        nonTerminals.add(new NonTerminal("B"));
    }

    @Override
    protected void setUpProductionRules(List<ProductionRule> productionRules) {
        productionRules.add(new ProductionRule(
            new NonTerminal("E"),
            new LexicalElement[] {
                new NonTerminal("E"),
                new Token("+"),
                new NonTerminal("B")
            }));

        productionRules.add(new ProductionRule(
            new NonTerminal("E"),
            new LexicalElement[] {
                new NonTerminal("E"),
                new Token("*"),
                new NonTerminal("B")
            }));

        productionRules.add(new ProductionRule(
            new NonTerminal("E"),
            new LexicalElement[] {
                new NonTerminal("B")
            }));

        productionRules.add(new ProductionRule(
            new NonTerminal("B"),
            new LexicalElement[] {
                new Token("0")
            }));

        productionRules.add(new ProductionRule(
            new NonTerminal("B"),
            new LexicalElement[] {
                new Token("1")
            }));
    }

    @Override
    protected void setUpStates(List<State> states, ProductionRule extraRootRule) {
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 0),
                new GrammarPosition(getRule(1), 0),
                new GrammarPosition(getRule(0), 0),
                new GrammarPosition(getRule(2), 0),
                new GrammarPosition(getRule(3), 0),
                new GrammarPosition(getRule(4), 0),
            }),
            null
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 1),
                new GrammarPosition(getRule(1), 1),
                new GrammarPosition(getRule(0), 1),
            }),
            getState(0)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 2),
                new GrammarPosition(getRule(3), 0),
                new GrammarPosition(getRule(4), 0),
            }),
            getState(1)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 3),
            }),
            getState(2)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 2),
                new GrammarPosition(getRule(3), 0),
                new GrammarPosition(getRule(4), 0),
            }),
            getState(1)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 3),
            }),
            getState(3)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 1),
            }),
            getState(0)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(3), 1),
            }),
            getState(0)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(4), 1),
            }),
            getState(0)
        ));

        //Tree branches
        getState(0)
            .addBranch(new Route(getState(1), new NonTerminal("E")))
            .addBranch(new Route(getState(6), new NonTerminal("B")))
            .addBranch(new Route(getState(7), new Token("0")))
            .addBranch(new Route(getState(8), new Token("1")));

        getState(1)
            .addBranch(new Route(getState(4), new Token("+")))
            .addBranch(new Route(getState(2), new Token("*")));

        getState(2)
            .addBranch(new Route(getState(3), new NonTerminal("B")));

        getState(4)
            .addBranch(new Route(getState(5), new NonTerminal("B")));

        //Graph branches, links to existing states
        getState(2)
            .addBranch(new Route(getState(7), new Token("0")))
            .addBranch(new Route(getState(8), new Token("1")));
            
        getState(4)
            .addBranch(new Route(getState(7), new Token("0")))
            .addBranch(new Route(getState(8), new Token("1")));
    }

    @Override
    protected void setUpActionTable(Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
        List<Token> allTokens = new ArrayList<>();
        allTokens.addAll(tokens);
        allTokens.add(endOfFile);

        Map<Token, Action> stateActions = actionTable.get(getState(0));
        stateActions.put(new Token("0"), new Shift(getState(7)));
        stateActions.put(new Token("1"), new Shift(getState(8)));

        stateActions = actionTable.get(getState(1));
        stateActions.put(new Token("*"), new Shift(getState(2)));
        stateActions.put(new Token("+"), new Shift(getState(4)));
        stateActions.put(endOfFile, new Accept());
        
        stateActions = actionTable.get(getState(2));
        stateActions.put(new Token("0"), new Shift(getState(7)));
        stateActions.put(new Token("1"), new Shift(getState(8)));
        
        stateActions = actionTable.get(getState(3));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(1)));
        }
        
        stateActions = actionTable.get(getState(4));
        stateActions.put(new Token("0"), new Shift(getState(7)));
        stateActions.put(new Token("1"), new Shift(getState(8)));
        
        stateActions = actionTable.get(getState(5));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(0)));
        }
        
        stateActions = actionTable.get(getState(6));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(2)));
        }

        stateActions = actionTable.get(getState(7));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(3)));
        }

        stateActions = actionTable.get(getState(8));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(4)));
        }
    }

    @Override
    protected void setUpGotoTable(Map<State, Map<NonTerminal, State>> gotoTable) {
        Map<NonTerminal, State> currentGotoActions = new HashMap<>();

        currentGotoActions.put(new NonTerminal("E"), getState(1));
        currentGotoActions.put(new NonTerminal("B"), getState(6));
        gotoTable.put(getState(0), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("B"), getState(5));
        gotoTable.put(getState(4), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("B"), getState(3));
        gotoTable.put(getState(2), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();
    }


    public ParseState getParseRoot(String sentence) {
        switch(sentence) {
            case "1+0*1":
                return parseTree0();
            
            case "1":
                return parseTree1();
            
            case "emptyReduce":
                return parseTree2();

            case "1+0*1MissingReduction":
                return parseTree3();
            
            default:
                throw new UnsupportedSentenceException("parse tree", sentence);
        }
    }

    /**
     * Parse tree for the sentence "1+0*1"
     * @return The root ParseState of the tree
     */
    private ParseState parseTree0() {
        List<ParseState> parseStates = new ArrayList<>();

        parseStates.add(new ShiftedState(getState(8), new Token("1")));


        parseStates.add(new ReducedState(getState(6), getRule(4), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(0)
                                                                                                    })));                                                                                            
        parseStates.add(new ShiftedState(getState(7), new Token("0")));

        
        parseStates.add(new ReducedState(getState(1), getRule(2), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(1)
                                                                                                    })));
        parseStates.add(new ShiftedState(getState(4), new Token("+")));
        parseStates.add(new ReducedState(getState(5), getRule(3), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(2)
                                                                                                    })));
        parseStates.add(new ShiftedState(getState(8), new Token("1")));


        parseStates.add(new ReducedState(getState(1), getRule(0), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(3),
                                                                                                        parseStates.get(4),
                                                                                                        parseStates.get(5)
                                                                                                    })));
        parseStates.add(new ShiftedState(getState(2), new Token("*")));
        parseStates.add(new ReducedState(getState(3), getRule(4), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(6)
                                                                                                    })));


        parseStates.add(new ReducedState(getState(1), getRule(1), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(7),
                                                                                                        parseStates.get(8),
                                                                                                        parseStates.get(9)
                                                                                                    })));

        return parseStates.get(parseStates.size() - 1);
    }

    /**
     * Parse tree for the sentence "1"
     * @return The root ParseState of the tree
     */
    private ParseState parseTree1() {
        List<ParseState> parseStates = new ArrayList<>();

        parseStates.add(new ShiftedState(getState(8), new Token("1")));

        return parseStates.get(parseStates.size() - 1);
    }

    /**
     * Parse tree for the sentence "emptyReduce"
     * @return The root ParseState of the tree
     */
    private ParseState parseTree2() {
        List<ParseState> parseStates = new ArrayList<>();

        parseStates.add(new ReducedState(getState(1), getRule(1), Arrays.asList(new ParseState[] {
            null,
            null,
            null
        })));

        return parseStates.get(parseStates.size() - 1);
    }

    /**
     * Parse tree for the sentence "1+0*1MissingReduction"
     * @return The root ParseState of the tree
     */
    private ParseState parseTree3() {
        List<ParseState> parseStates = new ArrayList<>();

        parseStates.add(new ShiftedState(getState(8), new Token("1")));


        parseStates.add(new ReducedState(getState(6), getRule(4), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(0)
                                                                                                    })));                                                                                            
        parseStates.add(new ShiftedState(getState(7), new Token("0")));

        
        parseStates.add(new ReducedState(getState(1), getRule(2), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(1)
                                                                                                    })));
        parseStates.add(new ShiftedState(getState(4), new Token("+")));
        parseStates.add(new ReducedState(getState(5), getRule(3), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(2)
                                                                                                    })));
        parseStates.add(new ShiftedState(getState(8), new Token("1")));


        parseStates.add(new ReducedState(getState(1), getRule(0), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(3),
                                                                                                        parseStates.get(4),
                                                                                                        null //Missing  (Reduction)
                                                                                                    })));
        parseStates.add(new ShiftedState(getState(2), new Token("*")));
        parseStates.add(new ReducedState(getState(3), getRule(4), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(6)
                                                                                                    })));


        parseStates.add(new ReducedState(getState(1), getRule(1), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(7),
                                                                                                        parseStates.get(8),
                                                                                                        parseStates.get(9)
                                                                                                    })));

        return parseStates.get(parseStates.size() - 1);
    }

    @Override
    protected void setUpGenerationBookends(Map<String, Map<String, String[]>> generationBookendMap) {
        generationBookendMap.put("Java", new HashMap<>());
        generationBookendMap.get("Java").put("1+0*1", new String[] {
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println(",

            ");\n" +
            "\t}\n" +
            "}"
        });
        generationBookendMap.get("Java").put("1", new String[] {
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println(",

            ");\n" +
            "\t}\n" +
            "}"
        });
        generationBookendMap.get("Java").put("emptyReduce", new String[] {
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println(",

            ");\n" +
            "\t}\n" +
            "}"
        });
        generationBookendMap.get("Java").put("1+0*1MissingReduction", new String[] {
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println(",

            ");\n" +
            "\t}\n" +
            "}"
        });

        generationBookendMap.put("C", new HashMap<>());
        generationBookendMap.get("C").put("1+0*1", new String[] {
            "#include <stdio.h>\n" +
            "\n" +  
            "main()\n" +
            "\tprintf(",

            ");\n" +
            "}"
        });
    }

    @Override
    protected void setUpRuleConvertors(Map<String, Map<String, Map<ProductionRule, Generator>>> ruleConvertorMap) {
        ruleConvertorMap.put("Java", new HashMap<>());

        HashMap<ProductionRule, Generator> ruleConvertor = new HashMap<>();
        ruleConvertor.put(getRule(0), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //E->E+B
        ruleConvertor.put(getRule(1), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //E->E*B
        ruleConvertor.put(getRule(2), (elements) -> { return elements[0].getGeneration(); }); //E->B
        ruleConvertor.put(getRule(3), (elements) -> { return elements[0].getGeneration(); }); //B->0
        ruleConvertor.put(getRule(4), (elements) -> { return elements[0].getGeneration(); }); //B->1
        ruleConvertorMap.get("Java").put("1+0*1", ruleConvertor);

        ruleConvertor.put(getRule(0), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //E->E+B
        ruleConvertor.put(getRule(1), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //E->E*B
        ruleConvertor.put(getRule(2), (elements) -> { return elements[0].getGeneration(); }); //E->B
        ruleConvertor.put(getRule(3), (elements) -> { return elements[0].getGeneration(); }); //B->0
        ruleConvertor.put(getRule(4), (elements) -> { return elements[0].getGeneration(); }); //B->1
        ruleConvertorMap.get("Java").put("1", ruleConvertor);

        ruleConvertor.put(getRule(0), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //E->E+B
        ruleConvertor.put(getRule(1), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //E->E*B
        ruleConvertor.put(getRule(2), (elements) -> { return elements[0].getGeneration(); }); //E->B
        ruleConvertor.put(getRule(3), (elements) -> { return elements[0].getGeneration(); }); //B->0
        ruleConvertor.put(getRule(4), (elements) -> { return elements[0].getGeneration(); }); //B->1
        ruleConvertorMap.get("Java").put("emptyReduce", ruleConvertor);

        ruleConvertor.put(getRule(0), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //E->E+B
        ruleConvertor.put(getRule(1), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //E->E*B
        ruleConvertor.put(getRule(2), (elements) -> { return elements[0].getGeneration(); }); //E->B
        ruleConvertor.put(getRule(3), (elements) -> { return elements[0].getGeneration(); }); //B->0
        ruleConvertor.put(getRule(4), (elements) -> { return elements[0].getGeneration(); }); //B->1
        ruleConvertorMap.get("Java").put("1+0*1MissingReduction", ruleConvertor);


        ruleConvertorMap.put("C", new HashMap<>());

        ruleConvertor = new HashMap<>();
        ruleConvertor.put(getRule(0), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //E->E+B
        ruleConvertor.put(getRule(1), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //E->E*B
        ruleConvertor.put(getRule(2), (elements) -> { return elements[0].getGeneration(); }); //E->B
        ruleConvertor.put(getRule(3), (elements) -> { return elements[0].getGeneration(); }); //B->0
        ruleConvertor.put(getRule(4), (elements) -> { return elements[0].getGeneration(); }); //B->1
        ruleConvertorMap.get("C").put("1+0*1", ruleConvertor);
    }

    @Override
    protected void setUpCodeGenerations(Map<String, Map<String, String>> codeGenerations) {
        codeGenerations.put("Java", new HashMap<>());
        codeGenerations.get("Java").put("1+0*1",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println(1 + 0 * 1);\n" +
            "\t}\n" +
            "}"
        );
        codeGenerations.get("Java").put("1",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println(1);\n" +
            "\t}\n" +
            "}"
        );
        codeGenerations.get("Java").put("emptyReduce",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println();\n" +
            "\t}\n" +
            "}"
        );
        codeGenerations.get("Java").put("1+0*1MissingReduction",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println(1 + 0 * 1);\n" +
            "\t}\n" +
            "}"
        );

        codeGenerations.put("C", new HashMap<>());
        codeGenerations.get("C").put("1+0*1",
            "#include <stdio.h>\n" +
            "\n" +  
            "main()\n" +
            "\tprintf(1 + 0 * 1);\n" +
            "}"
        );
    }

}
