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
        HashMap<NonTerminal, Set<NonTerminal>> followNTSets = new HashMap<>();

        for(NonTerminal nonTerminal : nonTerminals) {
            followTokenSets.put(nonTerminal, new HashSet<>());
            followNTSets.put(nonTerminal, new HashSet<>());
        }

        followTokenSets.get(sentinel).add(new EOF());

        findAllFollowingElements(productionRules, followTokenSets, followNTSets);

        resolveConnectedSets(followTokenSets, followNTSets);

        return followTokenSets;
    }

    private static void findAllFollowingElements(
        Set<ProductionRule> productionRules, 
        HashMap<NonTerminal, Set<Token>> followTokenSets,
        HashMap<NonTerminal, Set<NonTerminal>> followNTSets
    ) {
        Token emptyToken = new Token("");

        for(ProductionRule rule : productionRules) {
            LexicalElement[] elements = rule.productionSequence();

            if(elements.length == 0) { continue; }

            for(int i = 0; i < elements.length - 1; i++) {
                if(elements[i] instanceof NonTerminal) {
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
                        followNTSets.get(elements[i]).add((NonTerminal)nextElement);
                    }
                }
            }

            LexicalElement lastElement = elements[elements.length - 1];
            if(lastElement instanceof NonTerminal) {
                followNTSets.get((NonTerminal)lastElement).add(rule.nonTerminal());
            }
        }
    }

    private static void resolveConnectedSets(HashMap<NonTerminal, Set<Token>> followTokenSets,
            HashMap<NonTerminal, Set<NonTerminal>> followNTSets) {
        boolean changesMade = false;

        do {
            changesMade = false;

            for (NonTerminal nonTerminal : followNTSets.keySet()) {
                for (NonTerminal nonTerminal2 : followNTSets.get(nonTerminal)) {
                    if(followTokenSets.get(nonTerminal).containsAll(followTokenSets.get(nonTerminal2))) {
                        continue;
                    }

                    followTokenSets.get(nonTerminal).addAll(
                        followTokenSets.get(nonTerminal2)
                    );
                    
                    changesMade = true;
                }
            }
        } while (changesMade);
    }

}
