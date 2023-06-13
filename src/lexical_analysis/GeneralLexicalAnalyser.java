package lexical_analysis;

import java.util.Map;

import grammar_objects.*;

public class GeneralLexicalAnalyser implements LexicalAnalyser {

    /** 
        Initial checking:
        User-definables must have mutually exclusive regex if used in identical rules

        During analysis:
            [ Methods of seperation:
            -   Flank user-definables by reserved symbols (punctuation, operaters, whitespace, start of file, or end of file),
            -   Start/end of neighbouring tokens have unique symbols (includes no overlap) (toggle),
            -   Fixed number of start symbols with a distinguishing centre
            -       (?? start section would be assumed to be part of this token, not the last one read. 
            -       Potentially being confusing. Maybe allow this feature to be toggled)
            ]
            user-definables cannot be reserved words
        
            Approaches:
                Pure lexical analysis
                Grammar assisted analysis (will be a lot slower)
                [These need comparing, to make seperate analysers]

        @param whitespaceDelimiters All string to be considered as whitespace (Will not be tokenised)
        @param stronglyReservedWords Words that cannot be part of a user-definable token. Examples: operators, punctuation, etc. (Not including whitespace)
        @param weaklyReservedWords Words that will be tokenised if they do not appear as part of a user-definable token
    */
    GeneralLexicalAnalyser(
        String[] whitespaceDelimiters, //Could make this an array of strings if specific strings are considered whitespace/delimiters
        Token[] stronglyReservedWords,
        Token[] weaklyReservedWords,
        ProductionRule[] grammarRules,
        Map<Token, String> customTokenRegex,
        Map<Literal, String> literalRegex
    ) {
        //TODO
    }

    private void validateDynamicTokens() {

    }

    @Override
    public Token[] analyse(String sentence) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }
    
}
