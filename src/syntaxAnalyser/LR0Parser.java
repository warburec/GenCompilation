package syntaxAnalyser;

import java.util.*;

import GrammarObjects.*;

public class LR0Parser extends SyntaxAnalyser {

    protected Map<NonTerminal, Set<ProductionRule>> productionMap;
    protected Set<State> states;

    public LR0Parser(Set<Token> tokens, Set<NonTerminal> nonTerminals, Set<ProductionRule> productionRules, NonTerminal sentinel) {
        super(tokens, nonTerminals, productionRules, sentinel);
        generateProductionMap();
        generateTables();
    }

    public LR0Parser(Token[] tokens, NonTerminal[] nonTerminals, ProductionRule[] productionRules, NonTerminal sentinel) {
        super(tokens, nonTerminals, productionRules, sentinel);
        generateProductionMap();
        generateTables();
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

    private State createState(List<GrammarPosition> positions, State parentState) {
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
        List<NonTerminal> seenNonTerminals = new ArrayList<>();

        for (GrammarPosition position : state.getPositions()) {
            LexicalElement firstElement = position.rule().getFirstElement();
            if(!(firstElement instanceof NonTerminal)) { continue; }

            NonTerminal firstNonTerminal = (NonTerminal)firstElement;
            if(seenNonTerminals.contains(firstNonTerminal)) { continue; }

            for (ProductionRule rule : productionMap.get(firstNonTerminal)) {
                state.getPositions().add(new GrammarPosition(rule, 0));
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public GrammarStructure analyse(GrammarStructure grammarStructure) { //TODO change input/return types
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }
    
    public State[] getStates() {
        return (State[]) states.toArray();
    }
}
