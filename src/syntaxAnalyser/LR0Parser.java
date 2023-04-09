package syntaxAnalyser;

import java.util.HashMap;
import java.util.Map;

import GrammarObjects.*;

public class LR0Parser extends SyntaxAnalyser {

    protected Map<NonTerminal, ProductionRule> productionMap;

    LR0Parser(Token[] tokens, NonTerminal[] nonTerminals, ProductionRule[] productionRules, NonTerminal sentinel) {
        super(tokens, nonTerminals, productionRules, sentinel);

        generateProductionMap();
        generateTables();
    }

    private void generateProductionMap() {
        //TODO: Generate mapping of NonTerminals to their production rules
        productionMap = new HashMap<>();
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
