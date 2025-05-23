package component_construction.factories.lexical_analysis;

import lexical_analysis.*;

public interface LexicalAnalyserFactory {

    public abstract LexicalAnalyser produceAnalyser(
        String[] whitespaceDelimiters,
        String[] stronglyReservedWords,
        String[] weaklyReservedWords,
        DynamicTokenRegex[] dynamicTokenRegex
    );

}
