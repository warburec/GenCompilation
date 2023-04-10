package syntaxAnalyser;

import java.util.*;

import GrammarObjects.*;

public class LR0Parser extends SyntaxAnalyser {

    protected Map<NonTerminal, Set<ProductionRule>> productionMap;
    protected Set<State> states;

    protected Set<NonTerminal> invalidNonTerminals = Set.of(new NonTerminal[] {
                                                                new NonTerminal("Start")
                                                            });

    public LR0Parser(Set<Token> tokens, Set<NonTerminal> nonTerminals, Set<ProductionRule> productionRules, NonTerminal sentinel) {
        super(tokens, nonTerminals, productionRules, sentinel);
        checkForInvalidNonTerminals();
        generateProductionMap();
        generateTables();
    }

    public LR0Parser(Token[] tokens, NonTerminal[] nonTerminals, ProductionRule[] productionRules, NonTerminal sentinel) {
        super(tokens, nonTerminals, productionRules, sentinel);
        checkForInvalidNonTerminals();
        generateProductionMap();
        generateTables();
    }

    private void checkForInvalidNonTerminals() {
        for (NonTerminal nonTerminal : nonTerminals) {
            if(invalidNonTerminals.contains(nonTerminal)) {
                throw new RuntimeException("Reserved name " + nonTerminal.getName() + "Please use a different name for this non-terminal");
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
                //TODO: cause appropriate warning
            }

            productionMap.put(nonTerminal, rules);
        }
    }

    private void generateTables() {
        states = new HashSet<>();

        NonTerminal start = new NonTerminal("Start");
        LexicalElement[] startProductionSequence = new LexicalElement[] { sentinel };
        ProductionRule startProduction = new ProductionRule(start, startProductionSequence);

        GrammarPosition startPosition = new GrammarPosition(startProduction, 0);

        State rootState = createState(null, null);
        
        List<GrammarPosition> startPosList = Arrays.asList(new GrammarPosition[] { startPosition });
        formStateTree(rootState, startPosList);
    }

    private State createState(Set<GrammarPosition> positions, State parentState) {
        State newState = new State(positions, parentState);
        states.add(newState);

        return newState;
    }

    private State formStateTree(State state, List<GrammarPosition> startPositions) {
        for (GrammarPosition grammarPosition : startPositions) {
            state.getPositions().add(grammarPosition);
        }

        expandPositions(state);

        for (GrammarPosition grammarPosition : state.getPositions()) {
            if(grammarPosition.isClosed()) { continue; }

            GrammarPosition newPosition = grammarPosition.getNextPosition();
            LexicalElement traversedElement = grammarPosition.getNextElement();

            State existingState = getStateContainingPosition(newPosition);
            if(existingState != null) {
                state.addGraphBranch(new Route(existingState, traversedElement));
            }
            else {
                //State has not been seen before
                State newState = new State(null, state);
                formStateTree(newState, getNextPositionsForElement(state, traversedElement));

                newState.addTreeBranch(new Route(newState, traversedElement));
            }
        }

        return state;
    }

    private List<GrammarPosition> getNextPositionsForElement(State state, LexicalElement traversedElement) {
        List<GrammarPosition> nextPositions = new ArrayList<>();

        for (GrammarPosition position : state.getPositions()) {
            if(position.isClosed()) { continue; }

            if(position.getNextElement().equals(traversedElement)) {
                nextPositions.add(position.getNextPosition());
            }
        }

        return nextPositions;
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
     * Expands the existing positions in the given state based on the production rules.
     * All espansions will be positioned at the start of the production sequences.
     * @param state The state to expand the positions for
     */
    private void expandPositions(State state) {
        Set<NonTerminal> seenNonTerminals = new HashSet<>();


        List<GrammarPosition> positions = new ArrayList<>(state.getPositions());

        int i = 0;
        while(i < positions.size()) {
            GrammarPosition position = positions.get(i);

            LexicalElement firstElement = position.rule().getFirstElement();
            if(!(firstElement instanceof NonTerminal)) { i++; continue; }

            NonTerminal firstNonTerminal = (NonTerminal)firstElement;
            if(seenNonTerminals.contains(firstNonTerminal)) { i++; continue; }

            for (ProductionRule rule : productionMap.get(firstNonTerminal)) {
                GrammarPosition newPosition = new GrammarPosition(rule, 0);
                state.getPositions().add(newPosition);
                positions.add(newPosition);
            }

            seenNonTerminals.add(firstNonTerminal);

            i++;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public GrammarStructure analyse(GrammarStructure grammarStructure) { //TODO change input/return types
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }
    
    public Set<State> getStates() {
        return states;
    }
}
