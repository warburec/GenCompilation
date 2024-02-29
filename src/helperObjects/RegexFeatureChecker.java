package helperObjects;

import java.util.*;
import java.util.regex.*;

public class RegexFeatureChecker {

    // Works for simple bookends.
    /**
     * Produces regex for all starting and ending definite tokens (cannot be ommitted or repeated an unknown number of times)
     * @param regex The regex to be evaluated
     * @return Regex of definite starting and ending characters, null if bookends could not be found
     * @throws PatternSyntaxException Thrown when the input is invalid regex
     */
    public NotEmptyTuple<String, String> produceBookends(String regex) throws PatternSyntaxException {
        checkRegexIsValid(regex);
        
        String startRegex;
        String endRegex;

        String rule = "";

        String[] splitAtOrs = splitAtOr(regex);

        String fullStartRegex = "";
        String fullEndRegex = "";

        for(String regexPart : splitAtOrs) {
            //Split at (?:\\)?[^()\]\\]\?|(?:\\)[()\]]\? ".?" (not a lookahead or brackets)
            String[] splitString = regexPart.split("(?:\\\\)?[^()\\]\\\\]\\?|(?:\\\\)[()\\]]\\?");
            startRegex = splitString[0];
            endRegex = splitString[splitString.length - 1];

            //Split at matches of (?=[\\]?[^\\\)\]]\+)|(?=\\[)\]]\+) before ".+" not behind brackets
            rule = "(?=[\\\\]?[^\\\\\\)\\]]\\+)|(?=\\\\[)\\]]\\+)";
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

            //Handle at matching brackets before "*", "?", "+"(keep brackets) and forms of {.*[,...]?}
            splitString = handleGroupRepititions(startRegex, BookendType.Start);
            startRegex = splitString[0];
            splitString = handleGroupRepititions(endRegex, BookendType.End);
            endRegex = splitString[splitString.length - 1];

            //Remove "+"
            startRegex = startRegex.replace("+", "");
            endRegex = endRegex.replace("+", "");

            //Remove {1}, .{0}
            rule = "\\{1\\}|\\\\?.\\{0\\}";
            startRegex = startRegex.replaceAll(rule, "");
            endRegex = endRegex.replaceAll(rule, "");

            //Split at .{0,[0-9]*}, .{1,[0-9]*}, .{n(,[0-9]*)?} based on bookend type
            //Start: split at start .{0,...}, end of . for .{1,...}
            rule = "\\\\?.\\{0,[0-9]*\\}|(?=\\{1(?:,[0-9]*)?\\})";
            splitString = startRegex.split(rule);
            startRegex = splitString[0];
            //TODO: Split at end of .{n for .{n,...} + }

            //TODO: End: split at end .{0,...}, start of . for .{1,...} keep ., start of . for .{n,...} keep .{n}

            if(startRegex.equals("") || endRegex.equals("")) { return null; }
            if(startRegex.equals(regexPart)) { return null; }

            fullStartRegex += "|" + startRegex;
            fullEndRegex += "|" + endRegex;
        }

        // Remove leading '|'
        fullStartRegex = fullStartRegex.substring(1);
        fullEndRegex = fullEndRegex.substring(1);

        return new NotEmptyTuple<String,String>(fullStartRegex, fullEndRegex);
    }

    private void checkRegexIsValid(String regex) throws PatternSyntaxException {
        Pattern.compile(regex);
    }

    private String[] splitAtOr(String regex) {
        List<String> sections = new ArrayList<>();

        String currentString = "";
        int numOfBrackets = 0;

        for (int i = 0; i < regex.length(); i++) {
            char currentChar = regex.charAt(i);

            if(currentChar == '\\') {
                currentString += currentChar;
                i++;
                currentChar = regex.charAt(i);
            }
            else if(currentChar == '(' || currentChar == '[') { numOfBrackets++; }
            else if(currentChar == ')' || currentChar == ']') { numOfBrackets--; }
            else if(currentChar == '|' && numOfBrackets == 0) {
                sections.add(currentString);
                currentString = "";
                continue;
            }

            currentString += currentChar;
        }

        sections.add(currentString);

        return sections.toArray(new String[sections.size()]);
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

    private String[] handleGroupRepititions(String regex, BookendType bookendType) {
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
                        handleQuantified(features, bookendType);
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

    private void handleQuantified(QuantifierCheckFeatures features, BookendType bookendType) {
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
                features.regex = features.regex.substring(0, features.currentPosition + 1) + 
                    features.regex.substring(features.currentPosition + 4, features.regex.length());
                return;
            }

            case Range: {
                int closingIndex = features.regex.indexOf("}", features.currentPosition + 1);
                String range = features.regex.substring(features.currentPosition + 1, closingIndex + 1);

                String lowerBound = range.split(",")[0];

                switch(lowerBound) {
                    case "{0": {
                        features.regexParts.add(
                            features.regex.substring(features.currentPartBeginning, features.bracketStart)
                        );

                        features.currentPosition += range.length();
                        features.currentPartBeginning = features.currentPosition + 1;

                        return;
                    }

                    case "{1": {
                        features.regex = features.regex.substring(0, features.currentPosition) + 
                            features.regex.substring(features.currentPosition + range.length(), features.regex.length());
                        return;
                    }

                    default: { //{n,...}
                        if(lowerBound.endsWith("}")) {
                            lowerBound = lowerBound.substring(0, lowerBound.length() - 1);
                        }

                        switch(bookendType) {
                            case Start: {
                                features.regexParts.add(
                                    features.regex.substring(features.currentPartBeginning, features.currentPosition + 1) + lowerBound + "}"
                                );

                                features.currentPosition += range.length();
                                features.currentPartBeginning = features.currentPosition + 1;

                                return;
                            }

                            case End: {
                                features.regexParts.add(
                                    features.regex.substring(features.currentPartBeginning, features.bracketStart)
                                );

                                features.regex = features.regex.substring(0, features.currentPosition + 1) + 
                                    lowerBound + "}" +
                                    features.regex.substring(features.currentPosition + range.length() + 1, features.regex.length());
                                return;
                            }
                        }
                    }
                }
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

    private enum BookendType {
        Start,
        End
    }
}
