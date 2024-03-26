package builders.concrete_factories;

import builders.LexicalAnalyserFactory;
import lexical_analysis.*;

public class GeneralLexicalAnalyserFactory extends LexicalAnalyserFactory {

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
