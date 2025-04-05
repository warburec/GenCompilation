package component_construction.factories.lexical_analysis;

import lexical_analysis.*;

public class GeneralLexicalAnalyserFactory implements LexicalAnalyserFactory {

    @Override
    public LexicalAnalyser produceAnalyser(
        String[] whitespaceDelimiters, 
        String[] stronglyReservedWords,
        String[] weaklyReservedWords, 
        DynamicTokenRegex[] dynamicTokenRegex
    ) {
        return new GeneralLexicalAnalyser(
            whitespaceDelimiters, 
            stronglyReservedWords, 
            weaklyReservedWords, 
            dynamicTokenRegex
        );
    }
    
}
