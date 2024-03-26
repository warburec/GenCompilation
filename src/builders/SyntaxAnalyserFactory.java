package builders;

import grammar_objects.*;
import syntax_analysis.SyntaxAnalyser;

public abstract class SyntaxAnalyserFactory {
    
    public abstract SyntaxAnalyser produceAnalyser(GrammarParts parts);

}
