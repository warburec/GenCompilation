package lexical_analysis;

import grammar_objects.Token;

public class GeneralLexicalAnalyser implements LexicalAnalyser {

    /*
        Initial checking:
        User-definables must have mutually exclusive regex if used in identical rules

        During analysis:
            [ Methods of seperation:
            -   Delimiters, 
            -   Flank user-definables by predefined tokens,
            -   Start/end of neighbouring tokens have unique symbols (includes no overlap),
            -   Fixed number of start symbols with a unique centre
            -       (?? start section would be assumed to be part of this token, not the last one read. 
            -       Potentially being confusing. Maybe allow this feature to be toggled)
            ]

    */

    @Override
    public Token[] analyse(String sentence) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }
    
}
