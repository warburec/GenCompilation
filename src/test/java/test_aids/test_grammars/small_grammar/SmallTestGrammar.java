package test_aids.test_grammars.small_grammar;

import java.util.*;
import grammar_objects.*;
import grammars.small_grammar.SmallGrammar;
import grammars.small_grammar.convertors.*;
import syntax_analysis.grammar_structure_creation.*;
import grammars.small_grammar.SmallGrammar;
import syntax_analysis.grammar_structure_creation.State;
import syntax_analysis.parsing.*;
import test_aids.*;
import test_aids.test_grammars.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * E->E+B
 * E->E*B
 * E->B
 * B->0
 * B->1
 */
public class SmallTestGrammar {

    Grammar grammar = SmallGrammar.produce();

    private ProductionRule getRule(int index) {
        return grammar.getRule(index);
    }

    public TestGrammar getLR0Grammar() {
        TestGrammarBuilder builder = new TestGrammarBuilder(grammar);

        List<Token> allTokens = new ArrayList<>();
        allTokens.addAll(grammar.getParts().tokens());
        allTokens.add(builder.endOfFile);

        List<State> states = setUpStates(builder.extraRootRule);

        return builder.setUp()
        .addStates(states)

        //Tree branches
        .selectState(states.get(0))
            .addBranch(new Route(states.get(1), new NonTerminal("E")))
            .addBranch(new Route(states.get(6), new NonTerminal("B")))
            .addBranch(new Route(states.get(7), new Token("0")))
            .addBranch(new Route(states.get(8), new Token("1")))
            .deselectState()

        .selectState(states.get(1))
            .addBranch(new Route(states.get(4), new Token("+")))
            .addBranch(new Route(states.get(2), new Token("*")))
            .deselectState()

        .selectState(states.get(2))
            .addBranch(new Route(states.get(3), new NonTerminal("B")))
            .deselectState()

        .selectState(states.get(4))
            .addBranch(new Route(states.get(5), new NonTerminal("B")))
            .deselectState()

        //Graph branches, links to existing states
        .selectState(states.get(2))
            .addBranch(new Route(states.get(7), new Token("0")))
            .addBranch(new Route(states.get(8), new Token("1")))
            .deselectState()
            
        .selectState(states.get(4))
            .addBranch(new Route(states.get(7), new Token("0")))
            .addBranch(new Route(states.get(8), new Token("1")))
            .deselectState()
        
        .commitStates()

        .selectState(states.get(0))
            .addAction(new Token("0"), new Shift(states.get(7)))
            .addAction(new Token("1"), new Shift(states.get(8)))
            .deselectState()

        .selectState(states.get(1))
            .addAction(new Token("*"), new Shift(states.get(2)))
            .addAction(new Token("+"), new Shift(states.get(4)))
            .addAction(builder.endOfFile, new Accept())
            .deselectState()
        
        .selectState(states.get(2))
            .addAction(new Token("0"), new Shift(states.get(7)))
            .addAction(new Token("1"), new Shift(states.get(8)))
            .deselectState()
        
        .selectState(states.get(3))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(1))
            )))
            .deselectState()
        
        .selectState(states.get(4))
            .addAction(new Token("0"), new Shift(states.get(7)))
            .addAction(new Token("1"), new Shift(states.get(8)))
            .deselectState()
        
        .selectState(states.get(5))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(0))
            )))
            .deselectState()
        
        .selectState(states.get(6))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(2))
            )))
            .deselectState()

        .selectState(states.get(7))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(3))
            )))
            .deselectState()

        .selectState(states.get(8))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(4))
            )))
            .deselectState()
        
        .selectState(states.get(0))
            .addGoto(new NonTerminal("E"), states.get(1))
            .addGoto(new NonTerminal("B"), states.get(6))
            .deselectState()
    
        .selectState(states.get(4))
            .addGoto(new NonTerminal("B"), states.get(5))
            .deselectState()
        
        .selectState(states.get(2))
            .addGoto(new NonTerminal("B"), states.get(3))
            .deselectState()
        
        .commitTables()

        .setParseTreeRoot("1+0*1", parseTree0(states))
        .setParseTreeRoot("1", parseTree1(states))
        .setParseTreeRoot("emptyReduce", parseTree2(states))
        .setParseTreeRoot("1+0*1MissingReduction", parseTree3(states))

        .commitParseTrees()
        
        .setRuleConvertor("Java 1+0*1", OpZtO.produce())
        .setRuleConvertor("Java 1", One.produce())
        .setRuleConvertor("Java emptyReduce", EmptyReduce.produce())
        .setRuleConvertor("Java 1+0*1MissingReduction", OpZtOMissingReduction.produce())

        .setRuleConvertor("C 1+0*1", COpZtO.produce())

        .commitRuleConvertors()

        .setCodeGenerations(setUpCodeGenerations())

        .commitCodeGenerations()

        .generateTestGrammar();
    }

    private List<State> setUpStates(ProductionRule extraRootRule) {
        List<State> states = new ArrayList<State>();

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
            states.get(0)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 2),
                new GrammarPosition(getRule(3), 0),
                new GrammarPosition(getRule(4), 0),
            }),
            states.get(1)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 3),
            }),
            states.get(2)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 2),
                new GrammarPosition(getRule(3), 0),
                new GrammarPosition(getRule(4), 0),
            }),
            states.get(1)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 3),
            }),
            states.get(4)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 1),
            }),
            states.get(0)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(3), 1),
            }),
            states.get(0)
        ));

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(4), 1),
            }),
            states.get(0)
        ));

        return states;
    }

    // @Override
    // protected void setUpActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token builder.endOfFile) {
    //     switch (type) {
    //         case LR0 -> lr0ActionTable(type, actionTable, builder.endOfFile);
    //         case SLR1 -> slr1ActionTable(type, actionTable, builder.endOfFile);
    //         case CLR1 -> { /* Unimplemented */ }
            
    //         default -> throw new UnsupportedGrammarException(type);
    //     }
    // }

    // private void slr1ActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token builder.endOfFile) {
    //     Map<Token, Action> selectState(states.get(0));
    //     .addAction(new Token("0"), new Shift(states.get(7)));
    //     .addAction(new Token("1"), new Shift(states.get(8)));

    //     selectState(states.get(1));
    //     .addAction(new Token("*"), new Shift(states.get(2)));
    //     .addAction(new Token("+"), new Shift(states.get(4)));
    //     .addAction(new EOF(), new Accept());
        
    //     selectState(states.get(2));
    //     .addAction(new Token("0"), new Shift(states.get(7)));
    //     .addAction(new Token("1"), new Shift(states.get(8)));
        
    //     selectState(states.get(3));
    //     .addAction(new Token("+"), new Reduction(getRule(1)));
    //     .addAction(new Token("*"), new Reduction(getRule(1)));
    //     .addAction(new EOF(), new Reduction(getRule(1)));
        
    //     selectState(states.get(4));
    //     .addAction(new Token("0"), new Shift(states.get(7)));
    //     .addAction(new Token("1"), new Shift(states.get(8)));
        
    //     selectState(states.get(5));
    //     .addAction(new Token("+"), new Reduction(getRule(0)));
    //     .addAction(new Token("*"), new Reduction(getRule(0)));
    //     .addAction(new EOF(), new Reduction(getRule(0)));
        
    //     selectState(states.get(6));
    //     .addAction(new Token("+"), new Reduction(getRule(2)));
    //     .addAction(new Token("*"), new Reduction(getRule(2)));
    //     .addAction(new EOF(), new Reduction(getRule(2)));

    //     selectState(states.get(7));
    //     .addAction(new Token("+"), new Reduction(getRule(3)));
    //     .addAction(new Token("*"), new Reduction(getRule(3)));
    //     .addAction(new EOF(), new Reduction(getRule(3)));

    //     selectState(states.get(8));
    //     .addAction(new Token("+"), new Reduction(getRule(4)));
    //     .addAction(new Token("*"), new Reduction(getRule(4)));
    //     .addAction(new EOF(), new Reduction(getRule(4)));
    // }

    // @Override
    // protected void setUpGotoTable(GrammarType type, Map<State, Map<NonTerminal, State>> gotoTable) {
    //     Map<NonTerminal, State> currentGotoActions = new HashMap<>();

    //     currentGotoActions.put(new NonTerminal("E"), states.get(1));
    //     currentGotoActions.put(new NonTerminal("B"), states.get(6));
    //     gotoTable.put(states.get(0), new HashMap<>(currentGotoActions));
    //     currentGotoActions.clear();

    //     currentGotoActions.put(new NonTerminal("B"), states.get(5));
    //     gotoTable.put(states.get(4), new HashMap<>(currentGotoActions));
    //     currentGotoActions.clear();

    //     currentGotoActions.put(new NonTerminal("B"), states.get(3));
    //     gotoTable.put(states.get(2), new HashMap<>(currentGotoActions));
    //     currentGotoActions.clear();
    // }

    /**
     * Parse tree for the sentence "1+0*1"
     * @return The root ParseState of the tree
     */
    private ParseState parseTree0(List<State> states) {
        List<ParseState> parseStates = new ArrayList<>();

        parseStates.add(new ShiftedState(states.get(8), new Token("1")));


        parseStates.add(new ReducedState(states.get(6), getRule(4), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(0)
                                                                                                    })));                                                                                            
        parseStates.add(new ShiftedState(states.get(7), new Token("0")));

        
        parseStates.add(new ReducedState(states.get(1), getRule(2), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(1)
                                                                                                    })));
        parseStates.add(new ShiftedState(states.get(4), new Token("+")));
        parseStates.add(new ReducedState(states.get(5), getRule(3), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(2)
                                                                                                    })));
        parseStates.add(new ShiftedState(states.get(8), new Token("1")));


        parseStates.add(new ReducedState(states.get(1), getRule(0), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(3),
                                                                                                        parseStates.get(4),
                                                                                                        parseStates.get(5)
                                                                                                    })));
        parseStates.add(new ShiftedState(states.get(2), new Token("*")));
        parseStates.add(new ReducedState(states.get(3), getRule(4), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(6)
                                                                                                    })));


        parseStates.add(new ReducedState(states.get(1), getRule(1), Arrays.asList(new ParseState[] {
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
    private ParseState parseTree1(List<State> states) {
        List<ParseState> parseStates = new ArrayList<>();

        parseStates.add(new ShiftedState(states.get(8), new Token("1")));

        return parseStates.get(parseStates.size() - 1);
    }

    /**
     * Parse tree for the sentence "emptyReduce"
     * @return The root ParseState of the tree
     */
    private ParseState parseTree2(List<State> states) {
        List<ParseState> parseStates = new ArrayList<>();

        parseStates.add(new ReducedState(states.get(1), getRule(1), Arrays.asList(new ParseState[] {
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
    private ParseState parseTree3(List<State> states) {
        List<ParseState> parseStates = new ArrayList<>();

        parseStates.add(new ShiftedState(states.get(8), new Token("1")));


        parseStates.add(new ReducedState(states.get(6), getRule(4), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(0)
                                                                                                    })));                                                                                            
        parseStates.add(new ShiftedState(states.get(7), new Token("0")));

        
        parseStates.add(new ReducedState(states.get(1), getRule(2), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(1)
                                                                                                    })));
        parseStates.add(new ShiftedState(states.get(4), new Token("+")));
        parseStates.add(new ReducedState(states.get(5), getRule(3), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(2)
                                                                                                    })));
        parseStates.add(new ShiftedState(states.get(8), new Token("1")));


        parseStates.add(new ReducedState(states.get(1), getRule(0), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(3),
                                                                                                        parseStates.get(4),
                                                                                                        null //Missing  (Reduction)
                                                                                                    })));
        parseStates.add(new ShiftedState(states.get(2), new Token("*")));
        parseStates.add(new ReducedState(states.get(3), getRule(4), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(6)
                                                                                                    })));


        parseStates.add(new ReducedState(states.get(1), getRule(1), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(7),
                                                                                                        parseStates.get(8),
                                                                                                        parseStates.get(9)
                                                                                                    })));

        return parseStates.get(parseStates.size() - 1);
    }

    protected Map<String, String> setUpCodeGenerations() {
        Map<String, String> generations = new HashMap<>();
        
        generations.put("Java 1+0*1",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println(1 + 0 * 1);\n" +
            "\t}\n" +
            "}"
        );

        generations.put("Java 1",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println(1);\n" +
            "\t}\n" +
            "}"
        );

        generations.put("Java emptyReduce",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println();\n" +
            "\t}\n" +
            "}"
        );

        generations.put("Java 1+0*1MissingReduction",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println(1 + 0 * 1);\n" +
            "\t}\n" +
            "}"
        );
                
        generations.put("C 1+0*1",
            "#include <stdio.h>\n" +
            "\n" +  
            "main()\n" +
            "\tprintf(1 + 0 * 1);\n" +
            "}"
        );

        return generations;
    }

}
