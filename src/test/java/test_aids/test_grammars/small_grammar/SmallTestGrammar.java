package test_aids.test_grammars.small_grammar;

import java.util.*;

import grammar_objects.*;
import grammars.small_grammar.SmallGrammar;
import grammars.small_grammar.convertors.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;
import test_aids.*;
import test_aids.test_grammars.*;

/**
 * E->E+B
 * E->E*B
 * E->B
 * B->0
 * B->1
 */
public class SmallTestGrammar extends TestGrammar {

    public SmallTestGrammar(GrammarType type) {
        super(type);
    }

    @Override
    protected Grammar setUpGrammar(GrammarType type) {
        return SmallGrammar.produce();
    }

    @Override
    protected void setUpStates(GrammarType type, List<State> states, ProductionRule extraRootRule, Token endOfFile) {
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
            getState(4)
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
    protected void setUpActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
        switch (type) {
            case LR0 -> lr0ActionTable(type, actionTable, endOfFile);
            case SLR1 -> slr1ActionTable(type, actionTable, endOfFile);
            case CLR1 -> { /* Unimplemented */ }
            
            default -> throw new UnsupportedGrammarException(type);
        }
    }

    private void lr0ActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
        List<Token> allTokens = new ArrayList<>();
        allTokens.addAll(grammar.getParts().tokens());
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

    private void slr1ActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
        Map<Token, Action> stateActions = actionTable.get(getState(0));
        stateActions.put(new Token("0"), new Shift(getState(7)));
        stateActions.put(new Token("1"), new Shift(getState(8)));

        stateActions = actionTable.get(getState(1));
        stateActions.put(new Token("*"), new Shift(getState(2)));
        stateActions.put(new Token("+"), new Shift(getState(4)));
        stateActions.put(new EOF(), new Accept());
        
        stateActions = actionTable.get(getState(2));
        stateActions.put(new Token("0"), new Shift(getState(7)));
        stateActions.put(new Token("1"), new Shift(getState(8)));
        
        stateActions = actionTable.get(getState(3));
        stateActions.put(new Token("+"), new Reduction(getRule(1)));
        stateActions.put(new Token("*"), new Reduction(getRule(1)));
        stateActions.put(new EOF(), new Reduction(getRule(1)));
        
        stateActions = actionTable.get(getState(4));
        stateActions.put(new Token("0"), new Shift(getState(7)));
        stateActions.put(new Token("1"), new Shift(getState(8)));
        
        stateActions = actionTable.get(getState(5));
        stateActions.put(new Token("+"), new Reduction(getRule(0)));
        stateActions.put(new Token("*"), new Reduction(getRule(0)));
        stateActions.put(new EOF(), new Reduction(getRule(0)));
        
        stateActions = actionTable.get(getState(6));
        stateActions.put(new Token("+"), new Reduction(getRule(2)));
        stateActions.put(new Token("*"), new Reduction(getRule(2)));
        stateActions.put(new EOF(), new Reduction(getRule(2)));

        stateActions = actionTable.get(getState(7));
        stateActions.put(new Token("+"), new Reduction(getRule(3)));
        stateActions.put(new Token("*"), new Reduction(getRule(3)));
        stateActions.put(new EOF(), new Reduction(getRule(3)));

        stateActions = actionTable.get(getState(8));
        stateActions.put(new Token("+"), new Reduction(getRule(4)));
        stateActions.put(new Token("*"), new Reduction(getRule(4)));
        stateActions.put(new EOF(), new Reduction(getRule(4)));
    }

    @Override
    protected void setUpGotoTable(GrammarType type, Map<State, Map<NonTerminal, State>> gotoTable) {
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

    @Override
    protected void setUpParseTrees(Map<String, ParseTreeBuilder> parseRootMap) {
        parseRootMap.put("1+0*1", () -> parseTree0());
        parseRootMap.put("1", () -> parseTree1());
        parseRootMap.put("emptyReduce", () -> parseTree2());
        parseRootMap.put("1+0*1MissingReduction", () -> parseTree3());
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
    protected void setUpRuleConvertors(GrammarType type, Map<String, Map<String, RuleConvertor>> ruleConvertorMap) {
        ruleConvertorMap.put("Java", new HashMap<>());

        ruleConvertorMap.get("Java").put("1+0*1", OpZtO.produce());
        ruleConvertorMap.get("Java").put("1", One.produce());
        ruleConvertorMap.get("Java").put("emptyReduce", EmptyReduce.produce());
        ruleConvertorMap.get("Java").put("1+0*1MissingReduction", OpZtOMissingReduction.produce());

        ruleConvertorMap.put("C", new HashMap<>());

        ruleConvertorMap.get("C").put("1+0*1", COpZtO.produce());
    }

    @Override
    protected void setUpCodeGenerations(GrammarType type, Map<String, Map<String, String>> codeGenerations) {
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
