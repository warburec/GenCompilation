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

        //Remove {1}
        rule = "{1}";
        startRegex = startRegex.replace(rule, "");
        endRegex = endRegex.replace(rule, "");

        //Handle at matching brackets before "*", "?", "+"(keep brackets) and forms of {.*[,...]?}
        splitString = handleGroupRepititions(startRegex);
        startRegex = splitString[0];
        splitString = handleGroupRepititions(endRegex);
        endRegex = splitString[splitString.length - 1];

        //Remove "+"
        startRegex = startRegex.replace("+", "");
        endRegex = endRegex.replace("+", "");

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

    private String[] handleGroupRepititions(String regex) {
        QuantifierCheckFeatures features = new QuantifierCheckFeatures(regex);

        int numLeftBrackets = 0;
        int numRightBrackets = 0;

        for (; features.currentPosition < features.regex.length(); features.currentPosition++) {
            char currentChar = features.currentChar();

            if(features.currentPosition > 0 && features.getCharAt(features.currentPosition - 1) == '\\') {
                continue;
            }

            if(currentChar == '(' || currentChar == '[') {
                numLeftBrackets++;

                if(features.bracketStart == -1) {
                    features.bracketStart = features.currentPosition;
                }
                
                continue;
            }

            if(currentChar == ')' || currentChar == ']') {
                numRightBrackets++;

                if(numLeftBrackets == numRightBrackets){
                    try {
                        handleQuantified(features);
                    } catch (StringIndexOutOfBoundsException e) {
                        //Do nothing, end of string
                    }

                    numLeftBrackets = 0;
                    numRightBrackets = 0;

                    features.bracketStart = -1;
                }
            }
        }

        if(features.currentPosition == features.regex.length()) {
            features.regexParts.add(features.regex.substring(features.currentPartBeginning, features.regex.length()));
        }

        return features.regexParts.toArray(new String[features.regexParts.size()]);
    };

    private void handleQuantified(QuantifierCheckFeatures features) {
        QuantifierType quantifierType = checkQuantified(features);

        switch(quantifierType) {
            case ZeroOrMore: {
                features.regexParts.add(
                    features.regex.substring(features.currentPartBeginning, features.bracketStart)
                );

                features.currentPosition += 1;
                features.currentPartBeginning = features.currentPosition + 1;

                return;
            }

            case OneOrMore: {
                features.regexParts.add(
                    features.regex.substring(features.currentPartBeginning, features.currentPosition + 1)
                );

                features.currentPosition += 1;
                features.currentPartBeginning = features.currentPosition + 1;

                return;
            }

            case ZeroRep: {
                features.regexParts.add(
                    features.regex.substring(features.currentPartBeginning, features.bracketStart)
                );

                features.currentPosition += 3;
                features.currentPartBeginning = features.currentPosition + 1;

                return;
            }

            case OneRep: {
                features.regex = features.regex.substring(0, features.currentPosition) + 
                    features.regex.substring(features.currentPosition + 4, features.regex.length() + 1);
                return;
            }

            case Range: {

                return;
            }

            default:
                return;
        }
    }

    private QuantifierType checkQuantified(QuantifierCheckFeatures features) {
        char nextChar = features.nextChar();

        switch(nextChar) {
            case '*':
                return QuantifierType.ZeroOrMore;
            case '?':
                return QuantifierType.ZeroOrMore;
            case '+':
                return QuantifierType.OneOrMore;
            case '{':
            {
                int closingIndex = features.regex.indexOf("}", features.currentPosition + 1);
                String repitition = features.regex.substring(features.currentPosition + 1, closingIndex + 1);

                switch(repitition) {
                    case "{0}":
                        return QuantifierType.ZeroRep;
                    case "{1}":
                        return QuantifierType.OneRep;
                    default:
                        return QuantifierType.Range;
                }
            }
            default:
                return QuantifierType.None;
        }
    }

    private class QuantifierCheckFeatures {
        public int currentPartBeginning = 0;
        public int bracketStart = -1;
        public int currentPosition = 0;
        public String regex;
        public List<String> regexParts = new ArrayList<>();

        public QuantifierCheckFeatures(String regex) {
            this.regex = regex;
        }

        public char currentChar() {
            return regex.charAt(currentPosition);
        }

        public char nextChar() {
            return regex.charAt(currentPosition + 1);
        }

        public char getCharAt(int index) {
            return regex.charAt(index);
        }
    }

    private enum QuantifierType {
        None,
        ZeroOrMore,
        OneOrMore,
        ZeroRep,
        OneRep,
        Range
    }
}
