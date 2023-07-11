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

        //Split at \\?[^()]\?|\\[()]\? ".?" (not a lookahead or brackets)
        String[] splitString = regex.split("\\\\?[^()]\\?|\\\\[()]\\?");
        startRegex = splitString[0];
        endRegex = splitString[splitString.length - 1];

        //Split at matches of ((?<!\\)\+(?![^(]*\))) "+" not in brackets
        rule = "((?<!\\\\)\\+(?![^(]*\\)))";
        splitString = startRegex.split(rule);
        startRegex = splitString[0];
        splitString = endRegex.split(rule);
        endRegex = splitString[splitString.length - 1];

        //Remove all remaining "+" (within brackets) and {1}
        rule = "\\+|\\{1\\}";
        startRegex = startRegex.replaceAll(rule, "");
        endRegex = endRegex.replaceAll(rule, "");
        
        //TODO: Try removing
        //Split at {1,.*}
        rule = "\\{1,.*\\}";
        splitString = startRegex.split(rule);
        startRegex = splitString[0];
        splitString = endRegex.split(rule);
        endRegex = splitString[splitString.length - 1];

        //Split at top-level brackets containing '*' or '?'
        splitString = splitAtBracketsContainingIndefinite(startRegex);
        startRegex = splitString[0];
        splitString = splitAtBracketsContainingIndefinite(endRegex);
        endRegex = splitString[splitString.length - 1];

        //Split at (\\?[^])\\]\*)|(\\\)\*) removing any characters before '*' (not including brackets unless escaped)
        rule = "(\\\\?[^])\\\\]\\*)|(\\\\\\)\\*)";
        splitString = startRegex.split(rule);
        startRegex = splitString[0];
        splitString = endRegex.split(rule);
        endRegex = splitString[splitString.length - 1];

        //Split at matching brackets before "*", "?" and {.*[,...]?} (convert latter to just the lower bound)
        splitString = splitAtIndefiniteGroupRepititions(startRegex);
        startRegex = splitString[0];
        splitString = splitAtIndefiniteGroupRepititions(endRegex);
        endRegex = splitString[splitString.length - 1];

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
                    beginning = i + 1;
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

    private String[] splitAtIndefiniteGroupRepititions(String regex) {
        List<String> splitParts = new ArrayList<>();

        int beginning = 0;
        int startBracketPos = -1;
        int startRepitition = -1;

        int numLeftBrackets = 0;
        int numRightBrackets = 0;

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

                if(regex.charAt(i + 1) == '*' || regex.charAt(i + 1) == '?') {
                    splitParts.add(regex.substring(beginning, startBracketPos));
                    
                    i++;
                    beginning = i + 1;
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
                String splitRange = range.split(",", 1)[0];

                if(splitRange.charAt(splitRange.length() - 1) == '}') {
                    startRepitition = -1;

                    if(regex.charAt(i - splitRange.length() - 1) == ')') {
                        startBracketPos = -1;
                    }

                    continue;
                }

                splitParts.add(
                    regex.substring(0, (i - range.length()) + splitRange.length()) + "}"
                );

                startRepitition = -1;
                continue;
            }
        }

        splitParts.add(regex.substring(beginning, regex.length()));

        return splitParts.toArray(new String[splitParts.size()]);
    };

}
