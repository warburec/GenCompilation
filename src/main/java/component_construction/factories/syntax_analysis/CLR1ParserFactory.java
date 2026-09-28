package component_construction.factories.syntax_analysis;

import grammar_objects.GrammarParts;
import syntax_analysis.*;
import syntax_analysis.parsing.parsers.CLR1Parser;

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
