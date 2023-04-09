package syntaxAnalyser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GrammarObjects.*;

public class LR0Parser extends SyntaxAnalyser {

    protected Map<NonTerminal, List<ProductionRule>> productionMap;

    LR0Parser(Token[] tokens, NonTerminal[] nonTerminals, ProductionRule[] productionRules, NonTerminal sentinel) {
        super(tokens, nonTerminals, productionRules, sentinel);

        generateProductionMap();
        generateTables();
    }

    private void generateProductionMap() {
        productionMap = new HashMap<>();

        for (NonTerminal nonTerminal : nonTerminals) {
            ArrayList<ProductionRule> rules = new ArrayList<>();

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
        NonTerminal start = new NonTerminal("Start");
        LexicalElement[] startProductionSequence = new LexicalElement[] { sentinel };
        ProductionRule startProduction = new ProductionRule(start, startProductionSequence);

        GrammarPosition startPosition = new GrammarPosition(startProduction, 0);

        State rootState = new State(null, null);
        
        formStateTree(rootState, new GrammarPosition[] { startPosition });
    }

    private State formStateTree(State state, GrammarPosition[] startPositions) {
        for (GrammarPosition grammarPosition : startPositions) {
            state.positions().add(grammarPosition);
        }

        expandPositions(state);

        return state;
    }

    /**
     * Expands the existing positions in the given state based on the production rules.
     * All espansions will be positioned at the start of the production sequences.
     * @param state The state to expand the positions for
     */
    private void expandPositions(State state) {
        List<NonTerminal> seenNonTerminals = new ArrayList<>();

        for (GrammarPosition position : state.positions()) {
            LexicalElement firstElement = position.rule().getFirstElement();
            if(!(firstElement instanceof NonTerminal)) { continue; }

            NonTerminal firstNonTerminal = (NonTerminal)firstElement;
            if(seenNonTerminals.contains(firstNonTerminal)) { continue; }

            for (ProductionRule rule : productionMap.get(firstNonTerminal)) {
                state.positions().add(new GrammarPosition(rule, 0));
            }
        }
    }

    @Override
    public GrammarStructure analyse(GrammarStructure grammarStructure) { //TODO change input/return types
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }
    
    
}
