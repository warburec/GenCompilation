package tests.testAids.GrammarGenerators;

import java.util.*;

import code_generation.Generator;
import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;

public class SmallTestGrammar extends TestGrammar {

    @Override
    protected Token[] setUpTokens() {
        return  new Token[] {
            new Token("0"),
            new Token("1"),
            new Token("*"),
            new Token("+")
        };
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("E");
    }

    @Override
    protected NonTerminal[] setUpNonTerminals() {
        return new NonTerminal[] {
            new NonTerminal("E"),
            new NonTerminal("B")
        };
    }

    @Override
    protected ProductionRule[] setUpProductionRules() {
        return new ProductionRule[] {
            new ProductionRule(new NonTerminal("E"),
                            new LexicalElement[] {
                                new NonTerminal("E"),
                                new Token("+"),
                                new NonTerminal("B")
                            }),
            new ProductionRule(new NonTerminal("E"),
                            new LexicalElement[] {
                                new NonTerminal("E"),
                                new Token("*"),
                                new NonTerminal("B")
                            }),
            new ProductionRule(new NonTerminal("E"),
                            new LexicalElement[] {
                                new NonTerminal("B")
                            }),
            new ProductionRule(new NonTerminal("B"),
                            new LexicalElement[] {
                                new Token("0")
                            }),
            new ProductionRule(new NonTerminal("B"),
                            new LexicalElement[] {
                                new Token("1")
                            })
        };
    }

    @Override
    protected void setUpStates(List<State> states, ProductionRule extraRootRule) {
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 0),
                new GrammarPosition(productionRules[1], 0),
                new GrammarPosition(productionRules[0], 0),
                new GrammarPosition(productionRules[2], 0),
                new GrammarPosition(productionRules[3], 0),
                new GrammarPosition(productionRules[4], 0),
            }),
            null
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 1),
                new GrammarPosition(productionRules[1], 1),
                new GrammarPosition(productionRules[0], 1),
            }),
            getState(0)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[1], 2),
                new GrammarPosition(productionRules[3], 0),
                new GrammarPosition(productionRules[4], 0),
            }),
            getState(1)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[1], 3),
            }),
            getState(2)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[0], 2),
                new GrammarPosition(productionRules[3], 0),
                new GrammarPosition(productionRules[4], 0),
            }),
            getState(1)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[0], 3),
            }),
            getState(3)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[2], 1),
            }),
            getState(0)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[3], 1),
            }),
            getState(0)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[4], 1),
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
    protected void setUpActionTable(Map<State, Action> actionTable) {
        Map<Token, State> currentStateActions = new HashMap<>();

        currentStateActions.put(new Token("0"), getState(7));
        currentStateActions.put(new Token("1"), getState(8));
        actionTable.put(getState(0), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        currentStateActions.put(new Token("*"), getState(2));
        currentStateActions.put(new Token("+"), getState(4));
        actionTable.put(getState(1), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();
        
        currentStateActions.put(new Token("0"), getState(7));
        currentStateActions.put(new Token("1"), getState(8));
        actionTable.put(getState(2), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();
        
        actionTable.put(getState(3), new ReduceAction(productionRules[1]));
        
        currentStateActions.put(new Token("0"), getState(7));
        currentStateActions.put(new Token("1"), getState(8));
        actionTable.put(getState(4), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();
        
        actionTable.put(getState(5), new ReduceAction(productionRules[0]));
        
        actionTable.put(getState(6), new ReduceAction(productionRules[2]));

        actionTable.put(getState(7), new ReduceAction(productionRules[3]));

        actionTable.put(getState(8), new ReduceAction(productionRules[4]));

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


    /**
     * Gets the root parse state (and contained parse tree) for a given sentence
     * @param sentence The sentence to be parsed
     * @return The root ParseState of the parse tree
     */
    public ParseState getParseRoot(String sentence) {
        switch(sentence) {
            case "1+0*1":
                return parseTree0();
            
            case "1":
                return parseTree1();
            
            case "emptyReduce":
                return parseTree2();
            
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


        parseStates.add(new ReducedState(getState(6), productionRules[4], Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(0)
                                                                                                    })));                                                                                            
        parseStates.add(new ShiftedState(getState(7), new Token("0")));

        
        parseStates.add(new ReducedState(getState(1), productionRules[2], Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(1)
                                                                                                    })));
        parseStates.add(new ShiftedState(getState(4), new Token("+")));
        parseStates.add(new ReducedState(getState(5), productionRules[3], Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(2)
                                                                                                    })));
        parseStates.add(new ShiftedState(getState(8), new Token("1")));


        parseStates.add(new ReducedState(getState(1), productionRules[0], Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(3),
                                                                                                        parseStates.get(4),
                                                                                                        parseStates.get(5)
                                                                                                    })));
        parseStates.add(new ShiftedState(getState(2), new Token("*")));
        parseStates.add(new ReducedState(getState(3), productionRules[4], Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(6)
                                                                                                    })));


        parseStates.add(new ReducedState(getState(1), productionRules[1], Arrays.asList(new ParseState[] {
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

        parseStates.add(new ReducedState(getState(1), productionRules[1], Arrays.asList(new ParseState[] {
            null,
            null,
            null
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
        ruleConvertor.put(productionRules[0], (elements) -> { return elements[0] + " " + elements[1] + " " + elements[2]; }); //E->E+B
        ruleConvertor.put(productionRules[1], (elements) -> { return elements[0] + " " + elements[1] + " " + elements[2]; }); //E->E*B
        ruleConvertor.put(productionRules[2], (elements) -> { return elements[0]; }); //E->B
        ruleConvertor.put(productionRules[3], (elements) -> { return elements[0]; }); //B->0
        ruleConvertor.put(productionRules[4], (elements) -> { return elements[0]; }); //B->1
        ruleConvertorMap.get("Java").put("1+0*1", ruleConvertor);

        ruleConvertor.put(productionRules[0], (elements) -> { return elements[0] + " " + elements[1] + " " + elements[2]; }); //E->E+B
        ruleConvertor.put(productionRules[1], (elements) -> { return elements[0] + " " + elements[1] + " " + elements[2]; }); //E->E*B
        ruleConvertor.put(productionRules[2], (elements) -> { return elements[0]; }); //E->B
        ruleConvertor.put(productionRules[3], (elements) -> { return elements[0]; }); //B->0
        ruleConvertor.put(productionRules[4], (elements) -> { return elements[0]; }); //B->1
        ruleConvertorMap.get("Java").put("1", ruleConvertor);

        ruleConvertor.put(productionRules[0], (elements) -> { return elements[0] + " " + elements[1] + " " + elements[2]; }); //E->E+B
        ruleConvertor.put(productionRules[1], (elements) -> { return elements[0] + " " + elements[1] + " " + elements[2]; }); //E->E*B
        ruleConvertor.put(productionRules[2], (elements) -> { return elements[0]; }); //E->B
        ruleConvertor.put(productionRules[3], (elements) -> { return elements[0]; }); //B->0
        ruleConvertor.put(productionRules[4], (elements) -> { return elements[0]; }); //B->1
        ruleConvertorMap.get("Java").put("emptyReduce", ruleConvertor);

        ruleConvertorMap.put("C", new HashMap<>());

        ruleConvertor = new HashMap<>();
        ruleConvertor.put(productionRules[0], (elements) -> { return elements[0] + " " + elements[1] + " " + elements[2]; }); //E->E+B
        ruleConvertor.put(productionRules[1], (elements) -> { return elements[0] + " " + elements[1] + " " + elements[2]; }); //E->E*B
        ruleConvertor.put(productionRules[2], (elements) -> { return elements[0]; }); //E->B
        ruleConvertor.put(productionRules[3], (elements) -> { return elements[0]; }); //B->0
        ruleConvertor.put(productionRules[4], (elements) -> { return elements[0]; }); //B->1
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
