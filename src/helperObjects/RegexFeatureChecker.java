package helperObjects;

import java.util.*;

public class RegexFeatureChecker {

    //Works for simple bookends.
    //TODO: Increase complexity to any definite characters
    /**
     * @param regex The regex to be evaluated
     * @return Regex of definite starting and ending characters, null if bookends could not be found
     */
    public NotEmptyTuple<String, String> produceBookends(String regex) {
        String startRegex;
        String endRegex;

        //TODO: Alter all regex to not be incorrectly affected by \ characters
        //All splits remove the delimiters used

        //Split at (?<!\()\? "?" (not a lookahead)
        String[] splitString = regex.split("(?<!\\()\\?");
        startRegex = splitString[0];
        endRegex = splitString[splitString.length - 1];

        //Split at matches of ((?<!\\)\+(?![^(]*\))) "+" not in brackets
        splitString = startRegex.split("((?<!\\\\)\\+(?![^(]*\\)))");
        startRegex = splitString[0];
        splitString = endRegex.split("((?<!\\\\)\\+(?![^(]*\\)))");
        endRegex = splitString[splitString.length - 1];

        //Remove all remaining "+" (within brackets) and {1}
        startRegex = startRegex.replaceAll("\\+|\\{1\\}", "");
        endRegex = endRegex.replaceAll("\\+|\\{1\\}", "");
        
        //Split at {1,.*}
        splitString = startRegex.split("\\{1,.*\\}");
        startRegex = splitString[0];
        splitString = endRegex.split("\\{1,.*\\}");
        endRegex = splitString[splitString.length - 1];

        //Split at \[+.*(?<!\\)\*.*\]+|\(+.*(?<!\\)\*.*\)+ any brackets with * inside
        splitString = startRegex.split("\\[+.*(?<!\\\\)\\*.*\\]+|\\(+.*(?<!\\\\)\\*.*\\)+");
        startRegex = splitString[0];
        splitString = endRegex.split("\\[+.*(?<!\\\\)\\*.*\\]+|\\(+.*(?<!\\\\)\\*.*\\)+");
        endRegex = splitString[splitString.length - 1];

        //Split at (?![]|)]\*)(?!\\.\*)(?=.\*) removing any * characters (not including brackets)
        splitString = startRegex.split("(?![]|)]\\*)(?!\\\\.\\*)(?=.\\*)");
        startRegex = splitString[0];
        splitString = endRegex.split("(?![]|)]\\*)(?!\\\\.\\*)(?=.\\*)");
        endRegex = splitString[splitString.length - 1];

        //Split at matching brackets before "*" and {.*[,...]?} (convert latter to just the lower bound)
        startRegex = removeIndefiniteRepititions(startRegex);
        endRegex = removeIndefiniteRepititions(endRegex);

        if(startRegex.equals("") || endRegex.equals("")) { return null; }
        if(startRegex.equals(regex)) { return null; }

        return new NotEmptyTuple<String,String>(startRegex, endRegex);
    }

    private String removeIndefiniteRepititions(String regex) {
        String stringStart = "";
        int startBracketPos = -1;
        int startRepitition = -1;

        for (int i = 0; i < regex.length(); i++) {
            if(startBracketPos == -1 && 
                regex.charAt(i) == '(' || regex.charAt(i) == '[') 
            {
                startBracketPos = i;
                continue;
            }

            if(regex.charAt(i) == '*') {
                stringStart = regex.substring(0, i + 1);
                break;
            }

            if(regex.charAt(i) == '{' && regex.charAt(i - 1) != '\\') {
                startRepitition = i;
            }

            if(regex.charAt(i) == '}' && regex.charAt(i - 1) != '\\') {
                String range = regex.substring(startRepitition, i + 1);
                String splitRange = range.split(",", 1)[0];

                if(splitRange.charAt(splitRange.length()) == '}') {
                    startRepitition = -1;

                    if(splitRange.charAt(i - splitRange.length() - 1) == ')') {
                        startBracketPos = -1;
                    }

                    continue;
                }

                stringStart = regex.substring(0, (i - range.length()) + splitRange.length()) + "}";
                break;
            }
        }

        return stringStart;
    };

}
