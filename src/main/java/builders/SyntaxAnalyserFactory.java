package builders;

import grammar_objects.*;
import syntax_analysis.SyntaxAnalyser;

public interface SyntaxAnalyserFactory {
    
    public SyntaxAnalyser produceAnalyser(GrammarParts parts);

}
