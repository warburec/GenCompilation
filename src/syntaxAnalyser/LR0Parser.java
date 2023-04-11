package syntaxAnalyser;

import java.util.*;

import GrammarObjects.*;
import tests.GrammarPositionTests;

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
                System.out.println("\u001B[33mWarning: Non terminal \"" + nonTerminal.getName() + "\" has no rules associated with it\u001B[0m");
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

        createNewState(null, List.of(new GrammarPosition[] {startPosition}));
    }

    private State createNewState(State parentState, List<GrammarPosition> startPositions) {
        List<GrammarPosition> positions = expandPositions(startPositions);
        Set<Route> graphBranches = new HashSet<>();

        for(int i = positions.size() - 1; i >= 0; i--) {
            GrammarPosition currentPosition = positions.get(i);
            State foundState = getStateContainingPosition(currentPosition);

            if(foundState != null) {
                graphBranches.add(new Route(foundState, currentPosition.getNextElement()));
                positions.remove(currentPosition);
            }
        }

        //All graphBranches and positions for the new state are found
        State newState = new State(new HashSet<>(positions), parentState);
        states.add(newState);

        graphBranches.forEach((branch) -> newState.addGraphBranch(branch));

        List<LexicalElement> elementsUsed = new ArrayList<>();

        for (GrammarPosition position : positions) {
            if(position.isClosed()) { continue; }

            LexicalElement nextElement = position.getNextElement();

            if(elementsUsed.contains(nextElement)) { continue; }

            List<GrammarPosition> nextPositions = new ArrayList<>();

            for (GrammarPosition currentPosition : positions) {
                if(currentPosition.isClosed()) { continue; }

                if(nextElement.equals(currentPosition.getNextElement())) {
                    nextPositions.add(currentPosition.getNextPosition());
                }
            }

            elementsUsed.add(nextElement);

            State createdState = createNewState(newState, nextPositions);
            newState.addTreeBranch(new Route(createdState, nextElement));
        }

        return newState;
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
