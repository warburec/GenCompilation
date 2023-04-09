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
        //TODO: Populate tree/graph
        return state;
    }

    @Override
    public GrammarStructure analyse(GrammarStructure grammarStructure) { //TODO change input/return types
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }
    
    
}
