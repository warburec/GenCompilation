package syntaxAnalyser;

import GrammarObjects.NonTerminal;
import GrammarObjects.ProductionRule;
import GrammarObjects.Token;

public class LR0Parser extends SyntaxAnalyser {

    LR0Parser(Token[] tokens, NonTerminal[] nonTerminals, ProductionRule[] productionRules) {
        super(tokens, nonTerminals, productionRules);
    }
    
}
