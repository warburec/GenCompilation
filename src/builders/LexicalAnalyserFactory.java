package builders;

import lexical_analysis.*;

public abstract class LexicalAnalyserFactory {

    public abstract LexicalAnalyser produceAnalyser(
        String[] whitespaceDelimiters,
        String[] stronglyReservedWords,
        String[] weaklyReservedWords,
        DynamicTokenRegex[] dynamicTokenRegex
    );

}
