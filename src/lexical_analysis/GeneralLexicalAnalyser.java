package lexical_analysis;

import java.util.Map;

import grammar_objects.*;

public class GeneralLexicalAnalyser implements LexicalAnalyser {

    /*
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
    */
    GeneralLexicalAnalyser(
        Token[] punctuation,
        Token[] operators,
        String whitespaceDelimiters, //Could make this an array of strings if specific strings are considered whitespace/delimiters
        ProductionRule[] grammaRules,
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
