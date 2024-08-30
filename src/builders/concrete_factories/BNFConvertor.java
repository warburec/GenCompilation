package builders.concrete_factories;

import java.util.ArrayList;

import builders.GrammarFactory;
import grammar_objects.*;

/**
 * A class for converting text written in BNF into Grammar objects
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gatherGrammarDetails'");
    }

    @Override
    public Grammar produceGrammar() {
        return constructedGrammar;
    }

    protected class GrammarDetailsHolder {
        public ArrayList<Token> tokenHolder = new ArrayList<>();
        public ArrayList<NonTerminal> nonTerminalHolder = new ArrayList<>();
        public ArrayList<ProductionRule> ruleHolder = new ArrayList<>();
    }
}
