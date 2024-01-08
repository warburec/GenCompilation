package tests.test_aids.test_grammars.basic_SLR1;

import java.util.*;

import grammar_objects.*;
import grammars.basic_SLR1.BasicSLR1Grammar;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;
import tests.test_aids.*;
import tests.test_aids.test_grammars.UnsupportedSentenceException;

/**
 * S –> AA    
 * A –> aA
 * A –> b
 */
public class BasicSLR1TestGrammar extends TestGrammar {

    public BasicSLR1TestGrammar(GrammarType type) {
        super(type);
    }

    @Override
    protected Grammar setUpGrammar(GrammarType type) {
        return new BasicSLR1Grammar();
    }

    @Override
    protected void setUpStates(GrammarType type, List<State> states, ProductionRule extraRootRule, Token endOfFile) {
        states.add(new State(
            Set.of(
                new GrammarPosition(extraRootRule, 0),
                new GrammarPosition(getRule(0), 0),
                new GrammarPosition(getRule(1), 0),
                new GrammarPosition(getRule(2), 0)
            ),
            null
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(extraRootRule, 1)
            ),
            getState(0)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(0), 1),
                new GrammarPosition(getRule(1), 0),
                new GrammarPosition(getRule(2), 0)
            ),
            getState(0)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(0), 2)
            ),
            getState(2)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(1), 1),
                new GrammarPosition(getRule(1), 0),
                new GrammarPosition(getRule(2), 0)
            ),
            getState(2)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(1), 2)
            ),
            getState(4)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(2), 1)
            ),
            getState(4)
        ));

        getState(0).addBranch(new Route(getState(1), new NonTerminal("S")));
        getState(0).addBranch(new Route(getState(2), new NonTerminal("A")));
        getState(0).addBranch(new Route(getState(4), new Token("a")));
        getState(0).addBranch(new Route(getState(6), new Token("b")));

        getState(2).addBranch(new Route(getState(3), new NonTerminal("A")));
        getState(2).addBranch(new Route(getState(4), new Token("a")));
        getState(2).addBranch(new Route(getState(6), new Token("b")));

        getState(4).addBranch(new Route(getState(5), new NonTerminal("A")));
        getState(4).addBranch(new Route(getState(4), new Token("a")));
        getState(4).addBranch(new Route(getState(6), new Token("b")));
    }
    
    @Override
    protected void setUpActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
        Map<Token, Action> stateActions = actionTable.get(getState(0));
        stateActions.put(new Token("a"), new Shift(getState(4)));
        stateActions.put(new Token("b"), new Shift(getState(6)));

        //Accept EOF at state 1
        stateActions = actionTable.get(getState(1));
        stateActions.put(new EOF(), new Accept());

        stateActions = actionTable.get(getState(2));
        stateActions.put(new Token("a"), new Shift(getState(4)));
        stateActions.put(new Token("b"), new Shift(getState(6)));

        stateActions = actionTable.get(getState(3));
        stateActions.put(new EOF(), new Reduction(getRule(0)));

        stateActions = actionTable.get(getState(4));
        stateActions.put(new Token("a"), new Shift(getState(4)));
        stateActions.put(new Token("b"), new Shift(getState(6)));

        stateActions = actionTable.get(getState(5));
        stateActions.put(new Token("a"), new Reduction(getRule(1)));
        stateActions.put(new Token("b"), new Reduction(getRule(1)));
        stateActions.put(new EOF(), new Reduction(getRule(1)));

        stateActions = actionTable.get(getState(6));
        stateActions.put(new Token("a"), new Reduction(getRule(2)));
        stateActions.put(new Token("b"), new Reduction(getRule(2)));
        stateActions.put(new EOF(), new Reduction(getRule(2)));
    }

    @Override
    protected void setUpGotoTable(GrammarType type, Map<State, Map<NonTerminal, State>> gotoTable) {
        Map<NonTerminal, State> currentGotoActions = gotoTable.get(getState(0));
        currentGotoActions.put(new NonTerminal("S"), getState(1));
        currentGotoActions.put(new NonTerminal("A"), getState(2));

        currentGotoActions = gotoTable.get(getState(2));
        currentGotoActions.put(new NonTerminal("A"), getState(3));

        currentGotoActions = gotoTable.get(getState(4));
        currentGotoActions.put(new NonTerminal("A"), getState(5));
    }

    //TODO: Make functions into observers (move this method to outside the implementations)
    @Override
    public ParseState getParseRoot(String sentence) {
        switch(sentence) {
            case "CompleteSentence":
                return completeSentenceParse();
            
            default:
                throw new UnsupportedSentenceException("parse tree", sentence);
        }
    }

     /**
     * Parse tree for the sentence "CompleteSentence"
     * @return The root ParseState of the tree
     */
    private ParseState completeSentenceParse() {
        List<ParseState> parseStates = new ArrayList<>();

        parseStates.add(new ShiftedState(getState(6), new Token("b")));


        parseStates.add(new ReducedState(getState(2), getRule(2), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(0)
                                                                                                    })));      

        parseStates.add(new ShiftedState(getState(4), new Token("a")));
        parseStates.add(new ShiftedState(getState(4), new Token("a")));
        parseStates.add(new ShiftedState(getState(4), new Token("a")));
        parseStates.add(new ShiftedState(getState(6), new Token("b")));

        
        parseStates.add(new ReducedState(getState(5), getRule(2), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(5)
                                                                                                    })));
        
        parseStates.add(new ReducedState(getState(5), getRule(1), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(4),
                                                                                                        parseStates.get(6)
                                                                                                    })));
        
        parseStates.add(new ReducedState(getState(5), getRule(1), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(3),
                                                                                                        parseStates.get(7)
                                                                                                    })));
        
        parseStates.add(new ReducedState(getState(3), getRule(1), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(2),
                                                                                                        parseStates.get(8)
                                                                                                    })));
        
        
        parseStates.add(new ReducedState(getState(1), getRule(0), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(1),
                                                                                                        parseStates.get(9)
                                                                                                    })));

        return parseStates.get(parseStates.size() - 1);
    }

    @Override
    protected void setUpRuleConvertors(GrammarType type, Map<String, Map<String, RuleConvertor>> ruleConvertorMap) {
        // Unimplemented
    }

    @Override
    protected void setUpCodeGenerations(GrammarType type, Map<String, Map<String, String>> codeGenerations) {
        // Unimplemented
    }
    
}
