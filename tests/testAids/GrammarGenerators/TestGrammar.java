package tests.testAids.GrammarGenerators;

import java.util.*;

import code_generation.Generator;
import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;

public class TestGrammar extends Grammar {
    
    List<State> states = new ArrayList<>();
    Map<String, Map<String, String>> codeGenerations;           //Language, <Sentence, Code>
    Map<String, Map<ProductionRule, Generator>> ruleConvertors; //Sentence, ruleConverterMap
    Map<String, String[]> generationBookends;                   //Sentence, {preGeneration, postGeneration}

    public TestGrammar() {
        setUpTokens();
        setUpNonTerminalsAndSentinal();
        setUpProductionRules();
        setUpStates();
        setUpRuleConvertors();
        setUpCodeGenerations();
        setUpGenerationBookends();
    }

    private void setUpTokens() {
        tokens = new Token[] {
            new Token("0"),
            new Token("1"),
            new Token("*"),
            new Token("+")
        };
    }

    private void setUpNonTerminalsAndSentinal() {
        sentinal = new NonTerminal("E");
        nonTerminals = new NonTerminal[] {
            new NonTerminal("E"),
            new NonTerminal("B")
        };
    }

    private void setUpProductionRules() {
        productionRules = new ProductionRule[] {
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
    

    private void setUpStates() {
        ProductionRule extraRootRule = new ProductionRule(null, new LexicalElement[] {sentinal});

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
            states.get(0)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[1], 2),
                new GrammarPosition(productionRules[3], 0),
                new GrammarPosition(productionRules[4], 0),
            }),
            states.get(1)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[1], 3),
            }),
            states.get(2)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[0], 2),
                new GrammarPosition(productionRules[3], 0),
                new GrammarPosition(productionRules[4], 0),
            }),
            states.get(1)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[0], 3),
            }),
            states.get(3)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[2], 1),
            }),
            states.get(0)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[3], 1),
            }),
            states.get(0)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[4], 1),
            }),
            states.get(0)
        ));

        //Tree branches
        states.get(0)
            .addBranch(new Route(states.get(1), new NonTerminal("E")))
            .addBranch(new Route(states.get(6), new NonTerminal("B")))
            .addBranch(new Route(states.get(7), new Token("0")))
            .addBranch(new Route(states.get(8), new Token("1")));
        states.get(1)
            .addBranch(new Route(states.get(4), new Token("+")))
            .addBranch(new Route(states.get(2), new Token("*")));
        states.get(2)
            .addBranch(new Route(states.get(3), new NonTerminal("B")));
        states.get(4)
            .addBranch(new Route(states.get(5), new NonTerminal("B")));

        //Graph branches, links to existing states
        states.get(2)
            .addBranch(new Route(states.get(7), new Token("0")))
            .addBranch(new Route(states.get(8), new Token("1")));
        states.get(4)
            .addBranch(new Route(states.get(7), new Token("0")))
            .addBranch(new Route(states.get(8), new Token("1")));
    }

    public Set<State> getStates() {
        return new HashSet<>(states);
    }


    public Map<State, Action> getActionTable() {
        Map<State, Action> actionMap = new HashMap<State, Action>();
        Map<Token, State> currentStateActions = new HashMap<>();

        currentStateActions.put(new Token("0"), states.get(7));
        currentStateActions.put(new Token("1"), states.get(8));
        actionMap.put(states.get(0), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        currentStateActions.put(new Token("*"), states.get(2));
        currentStateActions.put(new Token("+"), states.get(4));
        // currentStateActions.put(new Token(null), states.get()); //TODO: Decide how Accept should be handled
        actionMap.put(states.get(1), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();
        
        currentStateActions.put(new Token("0"), states.get(7));
        currentStateActions.put(new Token("1"), states.get(8));
        actionMap.put(states.get(2), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();
        
        actionMap.put(states.get(3), new ReduceAction(productionRules[1]));
        
        currentStateActions.put(new Token("0"), states.get(7));
        currentStateActions.put(new Token("1"), states.get(8));
        actionMap.put(states.get(4), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();
        
        actionMap.put(states.get(5), new ReduceAction(productionRules[0]));
        
        actionMap.put(states.get(6), new ReduceAction(productionRules[2]));

        actionMap.put(states.get(7), new ReduceAction(productionRules[3]));

        actionMap.put(states.get(8), new ReduceAction(productionRules[4]));

        return actionMap;
    }

    public Map<State, Map<NonTerminal, State>> getGotoTable() {
        Map<State, Map<NonTerminal, State>> gotoMap = new HashMap<>();
        Map<NonTerminal, State> currentGotoActions = new HashMap<>();

        currentGotoActions.put(new NonTerminal("E"), states.get(1));
        currentGotoActions.put(new NonTerminal("B"), states.get(6));
        gotoMap.put(states.get(0), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("B"), states.get(5));
        gotoMap.put(states.get(4), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("B"), states.get(3));
        gotoMap.put(states.get(2), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        return gotoMap;
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

        parseStates.add(new ShiftedState(states.get(8), new Token("1")));


        parseStates.add(new ReducedState(states.get(6), productionRules[4], Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(0)
                                                                                                    })));                                                                                            
        parseStates.add(new ShiftedState(states.get(7), new Token("0")));

        
        parseStates.add(new ReducedState(states.get(1), productionRules[2], Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(1)
                                                                                                    })));
        parseStates.add(new ShiftedState(states.get(4), new Token("+")));
        parseStates.add(new ReducedState(states.get(5), productionRules[3], Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(2)
                                                                                                    })));
        parseStates.add(new ShiftedState(states.get(8), new Token("1")));


        parseStates.add(new ReducedState(states.get(1), productionRules[0], Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(3),
                                                                                                        parseStates.get(4),
                                                                                                        parseStates.get(5)
                                                                                                    })));
        parseStates.add(new ShiftedState(states.get(2), new Token("*")));
        parseStates.add(new ReducedState(states.get(3), productionRules[4], Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(6)
                                                                                                    })));


        parseStates.add(new ReducedState(states.get(1), productionRules[1], Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(7),
                                                                                                        parseStates.get(8),
                                                                                                        parseStates.get(9)
                                                                                                    })));

        return parseStates.get(parseStates.size() - 1);
    }

    private void setUpGenerationBookends() {
        generationBookends = new HashMap<>();

        generationBookends.put("1+0*1", new String[] {
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println(",

            ");" +
            "\t}" +
            "}"
        });
    }

    public String[] getGenerationBookends(String sentence) {
        String[] bookends = generationBookends.get(sentence);

        if(bookends == null) { 
            throw new UnsupportedSentenceException("generation bookends", sentence);
        }

        return bookends;
    }

    private void setUpRuleConvertors() {
        ruleConvertors = new HashMap<>();

        //TODO
        //HashMap<ProductionRule, Generator> ruleConvertor = HashMap<>();
        //ruleConvertor.add(ProductionRule, (elements) -> { return String; })
        //ruleConvertors.add(sentence, ruleConvertor);
    }

    public Map<ProductionRule, Generator> getRuleConvertor(String sentence) {
        return null;
    }

    private void setUpCodeGenerations() {
        codeGenerations = new HashMap<>();

        codeGenerations.put("Java", new HashMap<>());
        codeGenerations.get("Java").put("1+0*1",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println(1+0*1);" +
            "\t}" +
            "}"
        );
    }

    public String getGeneratedCode(String sentence, String language) {
        String generatedCode = null;

        try {
            generatedCode = codeGenerations.get(language).get(sentence);
        }
        catch (NullPointerException e) {
            throw new UnsupportedSentenceException("langage and code generation", sentence);
        }

        if(generatedCode == null) {
            throw new UnsupportedSentenceException("code generation", sentence);
        }

        return generatedCode;
    }

}
