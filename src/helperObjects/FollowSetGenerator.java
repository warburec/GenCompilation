package helperObjects;

import java.util.*;

import grammar_objects.*;

public class FollowSetGenerator {
    
    public static HashMap<NonTerminal, Set<Token>> generate(
        Set<ProductionRule> productionRules, 
        Set<NonTerminal> nonTerminals, 
        NonTerminal sentinel,
        HashMap<NonTerminal, Set<Token>> firstSets
    ) {
        HashMap<NonTerminal, Set<Token>> followTokenSets = new HashMap<>();
        HashMap<NonTerminal, Set<NonTerminal>> followingNTSets = new HashMap<>();

        for(NonTerminal nonTerminal : nonTerminals) {
            followTokenSets.put(nonTerminal, new HashSet<>());
            followingNTSets.put(nonTerminal, new HashSet<>());
        }

        followTokenSets.get(sentinel).add(new EOF());

        Token emptyToken = new Token("");
        
        findAndCombineFollowingElements(productionRules, followTokenSets, followingNTSets, firstSets, emptyToken);

        resolveConnectedSets(followTokenSets, followingNTSets, emptyToken);

        return followTokenSets;
    }

    private static void findAndCombineFollowingElements(
        Set<ProductionRule> productionRules,
        HashMap<NonTerminal, Set<Token>> followTokenSets,
        HashMap<NonTerminal, Set<NonTerminal>> followingNTSets,
        HashMap<NonTerminal, Set<Token>> firstSets,
        Token emptyToken
    ) {
        Set<NonTerminal> emptyNonTerminals = findEmptyNonTerminals(productionRules, emptyToken);

        for(ProductionRule rule : productionRules) {
            LexicalElement[] elements = rule.productionSequence();

            if(elements.length == 0) { continue; }

            for(int i = 0; i < elements.length - 1; i++) {
                if(!(elements[i] instanceof NonTerminal)) { continue; }

                int offset = 1;
                LexicalElement nextElement = elements[i + offset];

                while(nextElement.equals(emptyToken)) {
                    offset++;
                    nextElement = elements[i + offset];
                }

                if(nextElement instanceof Token) {
                    followTokenSets.get(elements[i]).add((Token)nextElement);
                }
                else {
                    followTokenSets.get(elements[i]).addAll(
                        removeEmpty(firstSets.get((NonTerminal)nextElement), emptyToken)
                    );

                    if(emptyNonTerminals.contains((NonTerminal)nextElement)) {
                        followingNTSets.get(elements[i]).add((NonTerminal)nextElement);
                    }
                }
            }

            LexicalElement lastElement = elements[elements.length - 1];
            if(lastElement instanceof NonTerminal &&
                !rule.nonTerminal().equals((NonTerminal)lastElement)) 
            {
                followingNTSets.get((NonTerminal)lastElement).add(rule.nonTerminal());
            }
        }
    }

    private static void resolveConnectedSets(
        HashMap<NonTerminal, Set<Token>> followTokenSets,
        HashMap<NonTerminal, Set<NonTerminal>> followingNTSets,
        Token emptyToken
    ) {
        boolean keepChecking = false;

        do {
            keepChecking = false;

            for(NonTerminal nonTerminal : followingNTSets.keySet()) {
                for(NonTerminal nonTerminal2 : followingNTSets.get(nonTerminal)) {
                    if(followTokenSets.get(nonTerminal).containsAll(followTokenSets.get(nonTerminal2))) {
                        continue;
                    }

                    keepChecking = true;

                    followTokenSets.get(nonTerminal).addAll(followTokenSets.get(nonTerminal2));
                }
            }
        } while (keepChecking);

        for (NonTerminal nonTerminal : followingNTSets.keySet()) {
            followTokenSets.get(nonTerminal).remove(emptyToken);
        }
    }

    private static Set<NonTerminal> findEmptyNonTerminals(
        Set<ProductionRule> productionRules,
        Token emptyToken
    ) {
        Set<NonTerminal> emptyNonTerminals = new HashSet<>();

        for(ProductionRule rule : productionRules) {
            if(rule.productionSequence().length == 0 ||
                (rule.productionSequence().length == 1 && emptyToken.equals(rule.productionSequence()[0]))
            ) {
                emptyNonTerminals.add(rule.nonTerminal());
            }
        }

        return emptyNonTerminals;
    }
    
    private static Set<Token> removeEmpty(Set<Token> set, Token emptyToken) {
        Set<Token> tokens = new HashSet<>();

        for (Token token : set) {
            if(!emptyToken.equals(token)) {
                tokens.add(token);
            }
        }
        return tokens;
    }
}
