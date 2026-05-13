package syntax_analysis;

import grammar_objects.*;
import syntax_analysis.parsing.*;

public interface SyntaxAnalyser {

    public abstract ParseState analyse(Token[] inputTokens) throws ParseFailedException;

}