package builders.concrete_factories;

import java.util.*;

import builders.GrammarFactory;
import grammar_objects.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for converting text written in BNF into Grammar objects
 * Notes:
 * <ul>
 *  <li> Production rules must have only one non-terminal to the left of the arrow seperator "->". To use the arrow within a non-terminal, preface the non-terminal with "n:" (non-terminal tag) and follow it by a space.
 *  <li> To use whitespace at the end of lines for bnf notated within a text-block (tripple quoted i.e.""") use \s to note at least the last space character.
 * </ul>
 */
public class BNFConvertor implements GrammarFactory {
    private Grammar constructedGrammar;

    private Set<String> validEscapeSequences = Set.of(new String[] {
        "\\ ",
        "\\\n",
        "\\\t",
        "\\e",
        "\\|"
    });
    
    /**
     * Allows the production of a Grammar object from the given grammar written in <a href = https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form>Backus-Naur Form</a> (BNF).
     * BNF in this convertor contains a list of grammar rules (one per line) in the form <b>"non-terminal -> list of tokens or non-terminals sperated by space characters"</b>.
     * Note that the arrow seperator "->" must be surrounded by at least one non-escaped space character on either side.
     * 
     * <p>
     * <b>Non-terminals</b> are defined by starting their name with a capital letter, or by preceding their name by the non-terminal tag <b>"n:"</b>.
     * <b>Tokens</b> are defined by starting their name with a non-capital letter, or by preceding their name by the token tag <b>"t:"</b>.
     * </p>
     * 
     * <p>
     * Spaces and newline characters <b>may</b> be used within token and non-terminal names but must be preceded by "\\", to signify that it should not be used as a BNF seperator.
     * An empty token (containing no contents) may be created by using "\\e" or "t:" surrounded by space seperators.
     * </p>
     * @param bnf The grammar written in BNF form
     */
    public BNFConvertor(String bnf) {        
        GrammarDetailsHolder detailsHolder = gatherGrammarDetails(bnf);

        constructedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                for (Token token : detailsHolder.tokenHolder) {
                    tokenOrganiser.addToken(token);
                }
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return detailsHolder.sentinal;
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                for (NonTerminal nonTerminal : detailsHolder.nonTerminalHolder) {
                    nonTerminalOrganiser.addNonTerminal(nonTerminal);
                }
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                for (ProductionRule rule : detailsHolder.ruleHolder) {
                    ruleOrganiser.addRule(rule);
                }
            }
        };
    }

    private GrammarDetailsHolder gatherGrammarDetails(String bnf) {
        Set<Token> tokenHolder = new HashSet<>();
        Set<NonTerminal> nonTerminalHolder = new HashSet<>();
        ArrayList<ProductionRule> ruleHolder = new ArrayList<>();
        NonTerminal sentinal = null;

        String[] lines = bnf.split("(?<!\\\\) *\\R+"); //Split at new lines (with proceding spaces as long as they are not escaped)

        NonTerminal previousRuleNonTerminal = null;

        for (int lineNum = 0; lineNum < lines.length; lineNum++) {
            String line = lines[lineNum];
            
            checkForInvalidEscaping(line, lineNum);

            String rightHandSide;
            NonTerminal ruleNonTerminal;
            
            boolean lineIsAlternative = line.matches("[\s\t]*\\|.*");

            if (lineIsAlternative) {
                if (previousRuleNonTerminal == null)
                    throw new AlternativeWithoutNonTerminalException(line, lineNum);

                rightHandSide = line.replaceFirst("[\\s\\t]*\\|\s*", "");
                ruleNonTerminal = previousRuleNonTerminal;
            }
            else {
                String[] splitByArrow = splitByArrow(line, lineNum);

                String leftHandSide = splitByArrow[0];
                rightHandSide = splitByArrow[1];

                ruleNonTerminal = categoriseLeftHandSide(leftHandSide, lineNum);

                nonTerminalHolder.add(ruleNonTerminal);
            }

            String[] alternativeRHSs = findAlternatives(rightHandSide);

            for (String alternative : alternativeRHSs) {
                List<LexicalElement> parts = categoriseElements(alternative, lineNum + 1);

                if (lineNum == 0) { sentinal = (NonTerminal) ruleNonTerminal; }

                for (LexicalElement lexicalElement : parts) {
                    if (lexicalElement instanceof Token) { tokenHolder.add((Token) lexicalElement); }
                    else { nonTerminalHolder.add((NonTerminal) lexicalElement); }
                }

                ruleHolder.add(new ProductionRule(
                    (NonTerminal) ruleNonTerminal,
                    parts.toArray(new LexicalElement[parts.size()])
                ));
            }

            if (!lineIsAlternative)
                previousRuleNonTerminal = ruleNonTerminal;
        }

        return new GrammarDetailsHolder(sentinal, tokenHolder, nonTerminalHolder, ruleHolder);
    }

    private void checkForInvalidEscaping(String line, int lineNum) {
        Pattern pattern = Pattern.compile("\\\\.");
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            if (validEscapeSequences.contains(matcher.group())) continue;

            throw new InvalidEscapeCharacterException(
                line,
                lineNum,
                matcher.start()
            );
        }
    }

    /**
     * Categorises the NonTerminal of the left hand side of a production rule
     * @param leftHandSide The test for the left hand side of the rule
     * @param lineNumber The number of the line of the production rule
     * @return The generated NonTerminal
     */
    private NonTerminal categoriseLeftHandSide(String leftHandSide, int lineNumber) {
        if (leftHandSide.matches(".*[^\\\\] .*")) //Contains space; therefore, multiple non-terminals
            throw new NonTerminalOveruseException(leftHandSide, lineNumber); 

        if(leftHandSide.startsWith("n:"))
            leftHandSide = leftHandSide.replaceFirst("n:", "");

        leftHandSide = removeEscapeChars(leftHandSide);

        return new NonTerminal(leftHandSide);
    }

    /**
     * Finds alternative definitions from the right hand side of a rule
     * @param rightHandSide The right hand side of the rule to be parsed
     * @return The alternative definitions provided in the right hands side of the rule
     */
    private String[] findAlternatives(String rightHandSide) {
        return rightHandSide.split("(?<!\\\\) *\\| *"); //Split at any non-escaped pipe characters (and accompanying spaces)
    }

    /**
     * Makes a list of lexical elements within the given text
     * @param elementText The text containing the elements to be categorised
     * @param lineNumber The order number of the given text
     * @return The list of lexical elements within the text
     */
    private List<LexicalElement> categoriseElements(String elementText, int lineNumber) {
        ArrayList<LexicalElement> lexElements = new ArrayList<>();

        if(elementText.equals("")) {
            lexElements.add(new EmptyToken());
            return lexElements;
        }

        String[] remainingParts = elementText.split("(?<!\\\\) "); //Split by " " not preceded by "\"

        for (String part : remainingParts) {
            if(part.equals("\\e")) {
                lexElements.add(new EmptyToken());
                continue;
            }
            
            part = removeEscapeChars(part);

            if(part.startsWith("t:")) {
                part = part.replaceFirst("t:", "");

                if(part.equals(""))
                    lexElements.add(new EmptyToken());
                else
                    lexElements.add(new Token(part));
            }
            else if(part.startsWith("n:"))
                lexElements.add(new NonTerminal(part.replaceFirst("n:", "")));
            else if(part.matches("[A-Z](?:.|\\s)*"))
                lexElements.add(new NonTerminal(part));
            else
                lexElements.add(new Token(part));
        }

        return lexElements;
    }

    /**
     * Split the given line at first arrow preceded by a non-escaped space
     * @param line The line to be split
     * @param lineNumber The line number of the given line
     * @return An array of the two parts either side of the first arrow
     */
    private String[] splitByArrow(String line, int lineNumber) {
        String[] splitByArrow = line.split("(?<!\\\\) +-> *", 2); //Split at first arrow preceded by a non-escaped space

        if (splitByArrow.length == 1) { throw new MissingArrowException(line, lineNumber); } //No split made

        return splitByArrow;
    }

    private String removeEscapeChars(String string) {
        return string.replaceAll("\\\\(?!\\\\)", ""); //Remove any \ not followed by a \ (last \ in a chain of \'s)
    }

    @Override
    public Grammar produceGrammar() {
        return constructedGrammar;
    }

    protected class GrammarDetailsHolder {
        public NonTerminal sentinal;
        public ArrayList<Token> tokenHolder;
        public ArrayList<NonTerminal> nonTerminalHolder;
        public ArrayList<ProductionRule> ruleHolder;

        public GrammarDetailsHolder(
            NonTerminal sentinal,
            Collection<Token> tokens, 
            Collection<NonTerminal> nonTerminals, 
            Collection<ProductionRule> rules
        ) {
            this.sentinal = sentinal;
            tokenHolder = new ArrayList<>(tokens);
            nonTerminalHolder = new ArrayList<>(nonTerminals);
            ruleHolder = new ArrayList<>(rules);
        }
    }

    public class MissingArrowException extends RuntimeException {
        public MissingArrowException(String line, int lineNumber) {
            super(
                "Line " + lineNumber + " has no valid arrow seperator (->). Please ensure an arrow has been placed preceded by a non-escaped space.\n" +
                "Line contents: " + line
            );
        }
    }

    public class NonTerminalOveruseException extends RuntimeException {
        public NonTerminalOveruseException(String line, int lineNumber) {
            super(
                "Line " + lineNumber + " has too many non-terminals on the left of the arrow seperator (->). Please ensure exactly one non-terminal preceded the seperator\n" +
                "Line contents: " + line
            );
        }
    }

    public class InvalidEscapeCharacterException extends RuntimeException {
        public InvalidEscapeCharacterException(String line, int lineNumber, int charNumber) {
            super(
                "Line " + lineNumber + " contains an invalid escape character at character " + charNumber + "\n" +
                "Line contents: " + line
            );
        }
    }

    public class AlternativeWithoutNonTerminalException extends RuntimeException {
        public AlternativeWithoutNonTerminalException(String line, int lineNumber) {
            super(
                "Line " + lineNumber + " contains an alternative statement without previously stating an initial production rule\n" +
                "Line contents: " + line
            );
        }
    }
}
