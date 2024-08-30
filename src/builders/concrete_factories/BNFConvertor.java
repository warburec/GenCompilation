package builders.concrete_factories;

import java.util.*;

import builders.GrammarFactory;
import grammar_objects.*;

/**
 * A class for converting text written in BNF into Grammar objects
 * Note: Production rules must have only one non-terminal to the left of the arrow seperator "->". To use the arrow within a non-terminal, preface the non-terminal with "n:" (non-terminal tag) and follow it by a space
 */
public class BNFConvertor implements GrammarFactory {
    private Grammar constructedGrammar;
    
    public BNFConvertor(String bnf){
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
                return detailsHolder.nonTerminalHolder.get(0); // First nonTerminal is the sentinal
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

        String[] lines = bnf.split("(?<!\\\\)(?> *\\R+)"); //Split at new lines (with proceding spaces as long ans they are not escaped)

        for (int i = 0; i < lines.length; i++) {
            List<LexicalElement> parts = categoriseParts(lines[i], i + 1);

            ruleHolder.add(new ProductionRule(
                (NonTerminal) parts.get(0),
                parts.subList(1, parts.size()).toArray(new LexicalElement[parts.size() - 1])
            ));

            for (LexicalElement lexicalElement : parts) {
                if (lexicalElement instanceof Token) { tokenHolder.add((Token) lexicalElement); }
                else { nonTerminalHolder.add((NonTerminal) lexicalElement); }
            }
        }

        return new GrammarDetailsHolder(tokenHolder, nonTerminalHolder, ruleHolder);
    }

    /**
     * Makes a list of lexical elements within a production rule, starting with the non-terminal for the rule
     * @param line The production rule line
     * @param lineNumber The order number of the given line
     * @return The list of lexical elements within the rule
     */
    private List<LexicalElement> categoriseParts(String line, int lineNumber) {
        ArrayList<LexicalElement> lexElements = new ArrayList<>();

        String[] splitByArrow = line.split("(?<!\\\\) +-> *", 2); //Split at first arrow preceded by a non-escaped space

        if (splitByArrow.length == 1) { throw new MissingArrowException(line, lineNumber); } //No split made

        String leftHandSide = splitByArrow[0];

        if (leftHandSide.matches(".*[^\\\\] .*")) //Contains space; therefore, multiple non-terminals
            throw new NonTerminalOveruseException(line, lineNumber); 

        if(leftHandSide.startsWith("n:"))
            leftHandSide = leftHandSide.replaceFirst("n:", "");

        leftHandSide = removeEscapeChars(leftHandSide);

        lexElements.add(new NonTerminal(leftHandSide));

        String[] remainingParts = splitByArrow[1].split("(?<!\\\\) "); //Split by " " not preceded by "\"

        for (String part : remainingParts) {
            part = removeEscapeChars(part);

            if(part.startsWith("t:")) { lexElements.add(new Token(part.replaceFirst("t:", ""))); }
            else if(part.startsWith("n:")) { lexElements.add(new NonTerminal(part.replaceFirst("n:", ""))); }
            else if(part.matches("[A-Z].*")) { lexElements.add(new NonTerminal(part)); }
            else { lexElements.add(new Token(part)); }
        }

        return lexElements;
    }

    private String removeEscapeChars(String string) {
        return string
            .replaceAll("\\\\\\\\", "\\\\") //Turn any "\\\\"" into "\\"
            .replaceAll("\\\\ ", " ") //Turn any "\\ " into " "
            .replaceAll("\\\\\n", "\n"); //Turn any "\\\n" into "\n"
    }

    @Override
    public Grammar produceGrammar() {
        return constructedGrammar;
    }

    protected class GrammarDetailsHolder {
        public ArrayList<Token> tokenHolder;
        public ArrayList<NonTerminal> nonTerminalHolder;
        public ArrayList<ProductionRule> ruleHolder;

        public GrammarDetailsHolder(
            Collection<Token> tokens, 
            Collection<NonTerminal> nonTerminals, 
            Collection<ProductionRule> rules
        ) {
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
}
