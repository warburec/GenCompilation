package lexical_analysis;

import grammar_objects.Token;

public interface LexicalAnalyser {
    public Token[] analyse(String sentence);
}
