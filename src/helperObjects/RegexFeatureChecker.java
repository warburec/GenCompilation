package helperObjects;

import java.util.*;

public class RegexFeatureChecker {

    // Works for simple bookends.
    /**
     * Produces regex for all starting and ending definite tokens (cannot be ommitted or repeated an unknown number of times)
     * @param regex The regex to be evaluated
     * @return Regex of definite starting and ending characters, null if bookends could not be found
     */
    public NotEmptyTuple<String, String> produceBookends(String regex) {
        String startRegex;
        String endRegex;

        String rule = "";

        //Split at (?:\\)?[^()\\]\?|(?:\\)[()]\? ".?" (not a lookahead or brackets)
        String[] splitString = regex.split("(?:\\\\)?[^()\\\\]\\?|(?:\\\\)[()]\\?");
        startRegex = splitString[0];
        endRegex = splitString[splitString.length - 1];

        //Split at matches of (?=[\\]?[^\\)]\+) before ".+" not behind brackets
        rule = "(?=[\\\\]?[^\\\\)]\\+)";
        splitString = startRegex.split(rule);
        startRegex = splitString[0];
        splitString = endRegex.split(rule);
        endRegex = splitString[splitString.length - 1];

        //Remove end "+"
        if(startRegex.equals("") || endRegex.equals("")) { return null; }
        if(startRegex.charAt(startRegex.length() - 1) == '+') {
            startRegex = startRegex.substring(0, startRegex.length() - 1);
        }
        if(endRegex.charAt(endRegex.length() - 1) == '+') {
            endRegex = endRegex.substring(0, endRegex.length() - 1);
        }

        //Split at top-level brackets containing '*' or '?'
        splitString = splitAtBracketsContainingIndefinite(startRegex);
        startRegex = splitString[0];
        splitString = splitAtBracketsContainingIndefinite(endRegex);
        endRegex = splitString[splitString.length - 1];

        //Split at (\\?[^])\\]\*)|(\\[)\]]\*) removing any characters before '*' (not including brackets unless escaped)
        rule = "(\\\\?[^])\\\\]\\*)|(\\\\[)\\]]\\*)";
        splitString = startRegex.split(rule);
        startRegex = splitString[0];
        splitString = endRegex.split(rule);
        endRegex = splitString[splitString.length - 1];

        //Split at matching brackets before "*", "?", "+"(keep brackets) and {.*[,...]?} (convert latter to just the lower bound)
        splitString = splitAtGroupRepititions(startRegex);
        startRegex = splitString[0];
        splitString = splitAtGroupRepititions(endRegex);
        endRegex = splitString[splitString.length - 1];        

        //Remove {1}
        rule = "{1}";
        startRegex = startRegex.replace(rule, "");
        endRegex = endRegex.replace(rule, "");

        if(startRegex.equals("") || endRegex.equals("")) { return null; }
        if(startRegex.equals(regex)) { return null; }

        return new NotEmptyTuple<String,String>(startRegex, endRegex);
    }

    private String[] splitAtBracketsContainingIndefinite(String regex) {
        List<String> stringParts = new ArrayList<>();

        int beginning = 0;
        int bracketStart = -1;
        int numLeftBrackets = 0;
        int numRightBrackets = 0;

        boolean checkForSplit = false;
        boolean indefiniteFound = false;

        for (int i = 0; i < regex.length(); i++) {
            if(i != 0 && regex.charAt(i - 1) == '\\') { continue; }

            if(bracketStart == -1) {
                if(regex.charAt(i) == '(' || regex.charAt(i) == '[') {
                    bracketStart = i;
                    numLeftBrackets = 1;
                }
                continue;
            }
            
            if(regex.charAt(i) == '*' || 
                (regex.charAt(i) == '?' && regex.charAt(i - 1) != '(')) 
            {
                indefiniteFound = true;
                continue;
            }

            if(regex.charAt(i) == '(' || regex.charAt(i) == '[') {
                numLeftBrackets++;
                
                if(numLeftBrackets == numRightBrackets) {
                    checkForSplit = true;
                }
            }
            else if(regex.charAt(i) == ')' || regex.charAt(i) == ']') {
                numRightBrackets++;

                if(numLeftBrackets == numRightBrackets) {
                    checkForSplit = true;
                }
            }

            if(checkForSplit) {
                if(indefiniteFound) {
                    stringParts.add(regex.substring(beginning, bracketStart));

                    //Remove following quantifier if given
                    if(regex.charAt(i + 1) == '*' || regex.charAt(i + 1) == '?') {
                        beginning = i + 2;
                        i++;
                    }
                    else if(regex.charAt(i + 1) == '{') {
                        i += 2;
                        while(regex.charAt(i) != '}') {
                            i++;
                        }
                        i++;

                        beginning = i;                        
                    }
                    else {
                        beginning = i + 1;
                    }
                }
                
                checkForSplit = false;
                bracketStart = -1;
                numLeftBrackets = 0;
                numRightBrackets = 0;
            }
        }

        stringParts.add(regex.substring(beginning, regex.length()));

        return stringParts.toArray(new String[stringParts.size()]);
    }

    private String[] splitAtGroupRepititions(String regex) {
        List<String> splitParts = new ArrayList<>();

        int beginning = 0;
        int startBracketPos = -1;
        int startRepitition = -1;

        int numLeftBrackets = 0;
        int numRightBrackets = 0;

        boolean suppressLastPart = false;

        for (int i = 0; i < regex.length(); i++) {
            if(i != 0 && regex.charAt(i - 1) == '\\') { continue; }

            if(regex.charAt(i) == '(' || regex.charAt(i) == '[')
            {
                numLeftBrackets++;

                if(startBracketPos == -1) {
                    startBracketPos = i;
                }

                continue;
            }
            
            if(regex.charAt(i) == ')' || regex.charAt(i) == ']') {
                numRightBrackets++;

                if(numLeftBrackets != numRightBrackets) { continue; }

                if(i == regex.length() - 1) { continue; }

                if(regex.charAt(i + 1) == '*' || 
                    regex.charAt(i + 1) == '?')
                {
                    splitParts.add(regex.substring(beginning, startBracketPos));
                    
                    i++;
                    beginning = i + 1;
                }
                else if(regex.charAt(i + 1) == '+') {
                    splitParts.add(regex.substring(beginning, i + 1));
                    
                    i++;
                    beginning = i + 1;

                    if(beginning == regex.length()) {
                        suppressLastPart = true;
                    }
                }

                startBracketPos = -1;
                startRepitition = -1;
                numLeftBrackets = 0;
                numRightBrackets = 0;

                continue;
            }

            if(regex.charAt(i) == '{') {
                startRepitition = i;
            }

            if(regex.charAt(i) == '}') {
                String range = regex.substring(startRepitition, i + 1);
                String[] splitRange = range.split(",", 2);

                if(splitRange[0].charAt(splitRange[0].length() - 1) == '}') {
                    startRepitition = -1;

                    if(i > splitRange[0].length() + 1 && 
                        regex.charAt((i - splitRange[0].length()) - 1) == ')')
                    {
                        startBracketPos = -1;
                    }

                    continue;
                }

                String remainder = regex.substring(i + 1, regex.length());
                regex = regex.substring(0, (i - range.length()) + splitRange[0].length() + 1) + "}" + remainder;

                i = i - splitRange[1].length();

                startRepitition = -1;
                continue;
            }
        }

        if(!suppressLastPart) {
            splitParts.add(regex.substring(beginning, regex.length()));
        }

        return splitParts.toArray(new String[splitParts.size()]);
    };

}
