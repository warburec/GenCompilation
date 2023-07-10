package helperObjects;

public class RegexFeatureChecker {

    //Works for simple bookends.
    //TODO: Increase complexity to any definite characters
    /**
     * @param regex The regex to be evaluated
     * @return Regex of definite starting and ending characters, null if bookends could not be found
     */
    public NotEmptyTuple<String, String> produceBookends(String regex) {
        String start;
        // Copy until first [, ., 
        // If ( found keep reference until ) found
        // If * or  { found : remove last character


        //TODO: Alter all regex to not be incorrectly affected by \ characters
        //All splits remove the delimiters used

        //Split at (?<!\()\? "?" (not a lookahead)
        //Split at matches of ((?<!\\)\+(?![^(]*\))) "+" not in brackets

        //Remove all remaining "+" (within brackets) and {1[,.*]?}

        //Split at \[+.*(?<!\\)\*.*\]+|\(+.*(?<!\\)\*.*\)+ any brackets with * inside

        //Split at (?![]|)]\*)(?!\\.\*)(?=.\*) removing any * characters (not including brackets)

        //Split at matching brackets before "*" and {0[,...]?}

        //Convert
        return null;
    };

}
