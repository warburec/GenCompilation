package syntax_analysis;

import java.util.*;

import grammar_objects.*;
import helperObjects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;

public class CLR1Parser extends SLR1Parser {

    // protected HashMap<NonTerminal, Set<Token>> firstSets;  //A map containing the first sets for all non-terminals

    private int currentParseToken = -1;

    protected static Token emptyToken = new EmptyToken();

    public CLR1Parser(Set<Token> tokens, Set<NonTerminal> nonTerminals, Set<ProductionRule> productionRules, NonTerminal sentinel) {
        super(tokens, nonTerminals, productionRules, sentinel);
    }

    public CLR1Parser(Token[] tokens, NonTerminal[] nonTerminals, ProductionRule[] productionRules, NonTerminal sentinel) {
        super(tokens, nonTerminals, productionRules, sentinel);
    }

    public CLR1Parser(Set<ProductionRule> productionRules, NonTerminal sentinel) {
        super(productionRules, sentinel);
    }

    public CLR1Parser(ProductionRule[] productionRules, NonTerminal sentinel) {
        super(productionRules, sentinel);
    }

    @Override
    protected void initialise() {
        generateFirstSets();
    }

    private void generateFirstSets() {
        firstSets = FirstSetGenerator.generate(productionRules, nonTerminals);
    }

    @Override
    protected void checkForInvalidNonTerminals() {
        for (NonTerminal nonTerminal : nonTerminals) {
            if(nonTerminal.getName().equals(null)) {
                throw new RuntimeException("Grammar cannot include a null non-terminal");
            }
        }
    }

    @Override
    protected void generateProductionMap() {
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

    @Override
    protected void generateStates() {
        states = new HashSet<>();

        NonTerminal start = null;
        LexicalElement[] startProductionSequence = new LexicalElement[] { sentinel };
        acceptRule = new ProductionRule(start, startProductionSequence);

        CLR1Position startPosition = new CLR1Position(acceptRule, 0, Set.of(EOF));

        rootState = createState(null, List.of(new CLR1Position[] {startPosition}), null);
    }

    @Override
    protected State createState(State parentState, List<GrammarPosition> startPositions, LexicalElement elemantTraversed) {
        List<GrammarPosition> currentPositions = startPositions;

        if(elemantTraversed != null) {
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
                currentState.addBranch(new Route(createdState, nextElement));
            }
        }

        return currentState;
    }

    /**
     * Gets all of the relevent positions after traversing the nextElement
     * @param currentPositions A List of all currentPositions
     * @param nextElement An element to be traversed
     * @return A List of new positions that traversed the given element
     */
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

    protected List<GrammarPosition> createParentGraphBranches(
        State parentState, 
        LexicalElement elementTraversed, 
        List<GrammarPosition> currentPositions
    ) {
        State foundLink = null;

        GrammarPosition firstPosition = currentPositions.get(0);

        State stateFound = getStateContainingPosition(firstPosition);

        if(stateFound != null) {
            Route newRoute = new Route(stateFound, elementTraversed);

            parentState.addBranch(newRoute);
            currentPositions.remove(currentPositions.size() - 1);
        }

        if(currentPositions.size() == 0) { return currentPositions; }

        foundLink = stateFound;

        GrammarPosition position;
        
        for(int i = 0; i < currentPositions.size(); i++) {
            position = currentPositions.get(i);

            stateFound = getStateContainingPosition(position);

            if(stateFound != foundLink) {
                throw new NonDeterminismException(elementTraversed, currentPositions, parentState);
            }
        }

        return currentPositions;
    }

    /**
     * Finds the state containing the given position
     * @param position The position to be found
     * @return The state containing the position, or null if no state is found
     */
    private State getStateContainingPosition(GrammarPosition position) {
        for (State state : states) {
            if(state.getPositions().contains(position)) {
                return state;
            }
        }

        return null;
    }

    /**
     * Expands the existing positions given based on the production rules, taking CLR the follow sets into account.
     * All espansions will be positioned at the start of the production sequences.
     * @param startPositions The positions to be expanded
     */
    private List<GrammarPosition> expandPositions(List<GrammarPosition> startPositions) {
        LinkedListHashMap<GrammarPosition, CombinedSet<Token>> positionMap = new LinkedListHashMap<>(); //TODO: Consider a different method than using composite sets
        initialiseLookaheadSets(startPositions, positionMap);
        
        LinkedMapIterator<GrammarPosition, CombinedSet<Token>> positionIterator = new LinkedMapIterator<>(positionMap);

        while(positionIterator.hasNext()) {
            GrammarPosition position = positionIterator.next();
            if(position.isClosed()) { continue; }

            LexicalElement nextElement = position.getNextElement();
            if(!(nextElement instanceof NonTerminal)) { continue; }

            NonTerminal nextNonTerminal = (NonTerminal)nextElement;

            CLR1Position posCLR1 = new CLR1Position(
                position.getRule(),
                position.getPosition(),
                positionMap.get(position).getValue()
            );

            CombinedSet<Token> lookahead = computeLookahead(posCLR1, positionMap.get(position));

            for(ProductionRule rule : productionMap.get(nextNonTerminal)) {
                GrammarPosition newPosition = new GrammarPosition(rule, 0);
                CombinedSet<Token> existingTokens = positionMap.get(newPosition);
                
                if(existingTokens == null) {
                    positionMap.put(newPosition, lookahead);
                }
                else {
                    existingTokens.addSet(lookahead);
                }
            }
        }

        return combinePosAndLookahead(positionMap);
    }

    private List<GrammarPosition> combinePosAndLookahead(LinkedListHashMap<GrammarPosition, CombinedSet<Token>> positionMap) {
        List<GrammarPosition> positionsWithLookahead = new ArrayList<>(positionMap.size());
        GrammarPosition[] finalPositions = positionMap.toArray(new GrammarPosition[positionMap.size()]);

        for(GrammarPosition positionKey : finalPositions) {
            Set<Token> lookahead = positionMap.get(positionKey).getValue();
            positionsWithLookahead.add(new CLR1Position(
                positionKey.getRule(),
                positionKey.getPosition(),
                lookahead
            ));
        }
        
        return positionsWithLookahead;
    }

    private void initialiseLookaheadSets(
        List<GrammarPosition> startPositions,
        LinkedListHashMap<GrammarPosition, CombinedSet<Token>> positions
    ) {
        for(int i = 0; i < startPositions.size(); i++) {
            CLR1Position clr1Position = (CLR1Position)startPositions.get(i);
            Set<Token> lookahead = clr1Position.getFollowSet();

            positions.put(clr1Position, new CombinedSet<>(lookahead));
        }
    }
    
    private CombinedSet<Token> computeLookahead(GrammarPosition position, CombinedSet<Token> positionLookahead) {
        CLR1Position clrPosition = (CLR1Position)position;
        LexicalElement[] productionSequence = clrPosition.getRule().productionSequence();

        CombinedSet<Token> lookahead = new CombinedSet<>();
        int seqPosition = clrPosition.getPosition() + 1;

        if(seqPosition >= productionSequence.length) {
            lookahead.addSet(positionLookahead);
            return lookahead;
        }

        boolean keepLooking = true;
        do {
            LexicalElement nextElement = productionSequence[seqPosition];

            if(nextElement instanceof Token) { 
                lookahead.addValue((Token)nextElement); 
                return lookahead;
            }

            Set<Token> firstSet = firstSets.get((NonTerminal)nextElement);

            lookahead.addSet(firstSet);

            if(!firstSet.contains(emptyToken)) {
                return lookahead;
            }

            if(seqPosition >= productionSequence.length) { 
                lookahead.addSet(positionLookahead);
                return lookahead; 
            }

            seqPosition++;
        } while(keepLooking);

        return lookahead;
    }

    public Set<State> getStates() {
        return states;
    }

    @Override
    protected void generateActionAndGotoTables() {
        actionTable = new HashMap<>();
        gotoTable = new HashMap<>();

        Set<Token> allTokens = new HashSet<>();
        allTokens.addAll(tokens);
        allTokens.add(EOF);

        for(State state : states) {
            actionTable.put(state, new HashMap<>());
            gotoTable.put(state, new HashMap<>());
        }

        for(State state : states) {
            //Reductions
            for(GrammarPosition position : state.getPositions()) {
                if(!position.isClosed()) { continue; }

                Set<Token> followingTokens = ((CLR1Position)position).getFollowSet();
                
                if(position.equals(new CLR1Position(acceptRule, 1, Set.of(EOF)))) { //Full accept Position
                    actionTable.get(state).put(EOF, new Accept());
                    continue;
                }

                Reduction reductionAction = new Reduction(position.getRule());

                Map<Token, Action> stateActions = actionTable.get(state);

                for(Token token : followingTokens) {
                    if(stateActions.get(token) == null) {
                        stateActions.put(token, reductionAction);
                    }
                    else {
                        List<ProductionRule> conflicts = new ArrayList<ProductionRule>();

                        Token storedReductionToken = actionTable.get(state).keySet().iterator().next();
                        conflicts.add(((Reduction)actionTable.get(state).get(storedReductionToken)).reductionRule());
                        conflicts.add(reductionAction.reductionRule());

                        throw new NonDeterminismException(conflicts, state);
                    }
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

            for(Token token : shiftActions.keySet()) {
                actionTable.get(state).put(token, new Shift(shiftActions.get(token)));
            }
            
            for(NonTerminal nonTerminal : gotoActions.keySet()) {
                gotoTable.get(state).put(nonTerminal, gotoActions.get(nonTerminal));
            } 
        }
    }

    public Map<State, Map<Token, Action>> getActionTable() {
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
                System.out.println(states.size());
                Action action = actionTable.get(parseStates.peek().state()).get(currentToken);

                if(action instanceof Shift) {
                    Shift shiftAction = (Shift)action;

                    parseStates.add(new ShiftedState(shiftAction.gotoState(), currentToken));

                    currentToken = getNextToken(input);
                }
                else if(action instanceof Reduction) {
                    Reduction reduceAction = (Reduction)action;

                    int stackSize = parseStates.size();
                    int numOfElements = reduceAction.reductionRule().productionSequence().length;
                    List<ParseState> statesToReduce = new ArrayList<>(parseStates.subList(stackSize - numOfElements, stackSize));

                    for(int i = 0; i < numOfElements; i++) {
                        parseStates.remove(stackSize - 1 - i);
                    }

                    State gotoState = gotoTable.get(parseStates.peek().state()).get(reduceAction.reductionRule().nonTerminal());
                    parseStates.add(new ReducedState(gotoState, reduceAction.reductionRule(), statesToReduce));
                }
                else if(action instanceof Accept) {
                    if(parseStates.size() > 2) {
                        throw new IncompleteParseException();
                    }

                    return parseStates.pop();
                }
                else {
                    if(currentToken.equals(EOF)) {
                        throw new IncompleteParseException();
                    }
                    
                    throw new SyntaxError(currentToken, parseStates.peek().state());
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