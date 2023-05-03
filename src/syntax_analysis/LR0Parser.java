package syntax_analysis;

import java.util.*;

import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;

public class LR0Parser extends SyntaxAnalyser {

    protected Map<NonTerminal, Set<ProductionRule>> productionMap;
    protected Set<State> states;
    protected State rootState;
    protected Map<State, Action> actionTable;
    protected Map<State, Map<NonTerminal, State>> gotoTable;
    protected ProductionRule acceptRule;

    public static final Token EOF = new Token(null);

    private int currentParseToken = -1;

    public LR0Parser(Set<Token> tokens, Set<NonTerminal> nonTerminals, Set<ProductionRule> productionRules, NonTerminal sentinel) {
        super(tokens, nonTerminals, productionRules, sentinel);
        checkForInvalidNonTerminals();
        generateProductionMap();
        generateStates();
        generateActionAndGotoTables();
    }

    public LR0Parser(Token[] tokens, NonTerminal[] nonTerminals, ProductionRule[] productionRules, NonTerminal sentinel) {
        super(tokens, nonTerminals, productionRules, sentinel);
        checkForInvalidNonTerminals();
        generateProductionMap();
        generateStates();
        generateActionAndGotoTables();
    }

    private void checkForInvalidNonTerminals() {
        for (NonTerminal nonTerminal : nonTerminals) {
            if(nonTerminal.getName().equals(null)) {
                throw new RuntimeException("Grammar cannot include a null non-terminal");
            }
        }
    }

    private void generateProductionMap() {
        productionMap = new HashMap<>();

        for (NonTerminal nonTerminal : nonTerminals) {
            Set<ProductionRule> rules = new HashSet<>();

            for (ProductionRule productionRule : productionRules) {
                if(nonTerminal.equals(productionRule.nonTerminal())) {
                    rules.add(productionRule);
                }
            }

            if(rules.isEmpty()) {
                System.out.println("\u001B[33mWarning: Non terminal \"" + nonTerminal.getName() + "\" has no rules associated with it\u001B[0m");
            }

            productionMap.put(nonTerminal, rules);
        }
    }


    private void generateStates() {
        states = new HashSet<>();

        NonTerminal start = null;
        LexicalElement[] startProductionSequence = new LexicalElement[] { sentinel };
        acceptRule = new ProductionRule(start, startProductionSequence);

        GrammarPosition startPosition = new GrammarPosition(acceptRule, 0);

        rootState = createState(null, List.of(new GrammarPosition[] {startPosition}), null);
    }

    private State createState(State parentState, List<GrammarPosition> startPositions, LexicalElement elemantTraversed) {
        List<GrammarPosition> currentPositions = startPositions;

        if(elemantTraversed != null) {
            //TODO: Ensure this is applied in the correct place, probably best after expansion (ensuring branches are only made once for the same value)
            //Make into order expand, graph branches, tree breanches
            currentPositions = createParentGraphBranches(parentState, elemantTraversed, currentPositions);
        }

        if(currentPositions.size() == 0) { return null; }

        currentPositions = expandPositions(startPositions);

        State currentState = new State(new HashSet<>(currentPositions), parentState);
        states.add(currentState);

        List<LexicalElement> elementsUsed = new ArrayList<>();

        for (GrammarPosition position : currentPositions) {
            if(position.isClosed()) { continue; }

            LexicalElement nextElement = position.getNextElement();

            if(elementsUsed.contains(nextElement)) { continue; }

            List<GrammarPosition> nextPositions = getNextPositionsTraversingElement(currentPositions, nextElement);
            elementsUsed.add(nextElement);

            State createdState = createState(currentState, nextPositions, nextElement);

            if(createdState != null) {
                //TODO:Throw Non-DeterminismException, if a route for element already exists?
                currentState.addBranch(new Route(createdState, nextElement));
            }
        }

        return currentState;
    }

    private List<GrammarPosition> getNextPositionsTraversingElement(List<GrammarPosition> currentPositions, LexicalElement nextElement) {
        List<GrammarPosition> nextPositions = new ArrayList<>();

        for (GrammarPosition currentPosition : currentPositions) {
            if(currentPosition.isClosed()) { continue; }

            if(nextElement.equals(currentPosition.getNextElement())) {
                nextPositions.add(currentPosition.getNextPosition());
            }
        }
        return nextPositions;
    }

    private List<GrammarPosition> createParentGraphBranches(State parentState, LexicalElement elemantTraversed, List<GrammarPosition> currentPositions) {
        //TODO:Create branch, only if all routes for the same lexEl goto the same place (no non-derterministic routes)
        for(int i = currentPositions.size() - 1; i >= 0; i--) {
            GrammarPosition position = currentPositions.get(i);

            State stateFound = getStateContainingPosition(position);

            if(stateFound != null) {
                //TODO:Throw Non-DeterminismException, if a route for element already exists?
                parentState.addBranch(new Route(stateFound, elemantTraversed));
                currentPositions.remove(i);
            }
        }

        return currentPositions;
    }

    private State getStateContainingPosition(GrammarPosition position) {
        for (State state : states) {
            if(state.getPositions().contains(position)) {
                return state;
            }
        }

        return null;
    }

    /**
     * Expands the existing positions given based on the production rules.
     * All espansions will be positioned at the start of the production sequences.
     * @param startPositions The positions to be expanded
     */
    private List<GrammarPosition> expandPositions(List<GrammarPosition> startPositions) {
        Set<NonTerminal> seenNonTerminals = new HashSet<>();

        List<GrammarPosition> positionsList = new ArrayList<>(startPositions);

        int i = 0;
        while(i < positionsList.size()) {
            GrammarPosition position = positionsList.get(i);
            if(position.isClosed()) { i++; continue; }

            LexicalElement nextElement = position.getNextElement();
            if(!(nextElement instanceof NonTerminal)) { i++; continue; }

            NonTerminal nextNonTerminal = (NonTerminal)nextElement;
            if(seenNonTerminals.contains(nextNonTerminal)) { i++; continue; }

            for (ProductionRule rule : productionMap.get(nextNonTerminal)) {
                GrammarPosition newPosition = new GrammarPosition(rule, 0);
                positionsList.add(newPosition);
            }

            seenNonTerminals.add(nextNonTerminal);

            i++;
        }

        return positionsList;
    }
    
    public Set<State> getStates() {
        return states;
    }


    private void generateActionAndGotoTables() {
        actionTable = new HashMap<>();
        gotoTable = new HashMap<>();

        for(State state : states) {
            //Reductions
            for(GrammarPosition position : state.getPositions()) {
                if(position.isClosed()) {
                    if(position.equals(new GrammarPosition(acceptRule, 1))) { //Full accept Position
                        continue;
                    }

                    actionTable.put(state, new ReduceAction(position.rule()));
                }
            }

            //Shifts and goto
            Map<Token, State> shiftActions = new HashMap<>();
            Map<NonTerminal, State> gotoActions = new HashMap<>();

            for(Route route : state.getBranches()) {
                if(route.elementTraversed() instanceof Token) {
                    //Shift action
                    shiftActions.put((Token)route.elementTraversed(), route.gotoState());
                }
                else if(route.elementTraversed() instanceof NonTerminal) {
                    //Goto actions
                    gotoActions.put((NonTerminal)route.elementTraversed(), route.gotoState());
                }
            }

            if(shiftActions.size() > 0) {
                actionTable.put(state, new ShiftAction(shiftActions));
            }
            if(gotoActions.size() > 0) {
                gotoTable.put(state, gotoActions);
            } 
        }
    }

    public Map<State, Action> getActionTable() {
        return actionTable;
    }

    public Map<State, Map<NonTerminal, State>> getGotoTable() {
        return gotoTable;
    }

    public ParseState analyse(Token[] inputTokens) throws ParseFailedException {
        Iterator<Token> input = Arrays.stream(inputTokens).iterator();
        boolean accepted = false;
        Stack<ParseState> parseStates = new Stack<>();

        parseStates.add(new ShiftedState(rootState, null));
        Token currentToken = getNextToken(input);

        try {
            while(!accepted) {
                Action action = actionTable.get(parseStates.peek().state());

                if(action instanceof ShiftAction) {
                    ShiftAction shiftAction = (ShiftAction)action;

                    if(currentToken.equals(EOF)) {
                        if(parseStates.peek().state().getPositions()
                            .contains(new GrammarPosition(acceptRule, 1))) { //Accept
                            currentParseToken = -1;
                            return parseStates.pop();
                        }
                        else {
                            throw new IncompleteParseException();
                        }
                    }

                    parseStates.add(new ShiftedState(shiftAction.getState(currentToken), currentToken));

                    currentToken = getNextToken(input);
                }
                else if (action instanceof ReduceAction) {
                    ReduceAction reduceAction = (ReduceAction)action;

                    int stackSize = parseStates.size();
                    int numOfElements = reduceAction.reductionRule().productionSequence().length;
                    List<ParseState> statesToReduce = new ArrayList<>(parseStates.subList(stackSize - numOfElements, stackSize));

                    for(int i = 0; i < numOfElements; i++) {
                        parseStates.remove(stackSize - 1 - i);
                    }

                    State gotoState = gotoTable.get(parseStates.peek().state()).get(reduceAction.reductionRule().nonTerminal());
                    parseStates.add(new ReducedState(gotoState, reduceAction.reductionRule(), statesToReduce));
                }
                else {
                    throw new UnsupportedActionException(action, parseStates.peek().state());
                }   
            }
        }
        catch(Exception e) {
            throw new ParseFailedException(e, currentParseToken);
        }

        return parseStates.pop();
    }

    private Token getNextToken(Iterator<Token> input) {
        currentParseToken++;

        try {
            return input.next();
        }
        catch(NoSuchElementException e) {
            return EOF;
        }
    }

    public class UnsupportedActionException extends Exception {
        private Action attemptedAction;
        private State currentState;

        public UnsupportedActionException(Action attemptedAction, State currentState) {
            super("Action type " + attemptedAction.getClass().getTypeName() + " not supported for the current state");

            this.attemptedAction = attemptedAction;
            this.currentState = currentState;
        }

        public Action getAttemptedAction() {
            return attemptedAction;
        }

        public State getCurrentState() {
            return currentState;
        }
    }
}
