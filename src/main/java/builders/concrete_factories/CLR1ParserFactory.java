package builders.concrete_factories;

import builders.SyntaxAnalyserFactory;
import grammar_objects.GrammarParts;
import syntax_analysis.*;

public class CLR1ParserFactory implements SyntaxAnalyserFactory {

    @Override
    public SyntaxAnalyser produceAnalyser(GrammarParts parts) {
        return new CLR1Parser(
            parts.tokens(),
            parts.nonTerminals(),
            parts.productionRules(),
            parts.sentinal()
        );
    }
    
}
