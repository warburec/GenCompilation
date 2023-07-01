package helperObjects;

import java.util.*;

import grammar_objects.*;

public class FirstSetGenerator {

    public static HashMap<NonTerminal, Set<Token>> generate(Set<ProductionRule> productionRules, Set<NonTerminal> nonTerminals) {
        HashMap<NonTerminal, Set<Token>> firstTokenSets = new HashMap<>();
        HashMap<NonTerminal, Set<NonTerminal>> firstNTSets = new HashMap<>();

        Set<NonTerminal> emptyNonTerminals = new HashSet<>();

        Token emptyToken = new Token("");

        for(NonTerminal nonTerminal : nonTerminals) {
            firstTokenSets.put(nonTerminal, new HashSet<>());
            firstNTSets.put(nonTerminal, new HashSet<>());
        }

        findAllFirstElements(productionRules, firstTokenSets, firstNTSets, emptyToken, emptyNonTerminals);

        resolveCompleteSets(firstTokenSets, firstNTSets);
        resolveConnectedSets(firstTokenSets, firstNTSets);

        for (NonTerminal nonTerminal : emptyNonTerminals) {
            firstTokenSets.get(nonTerminal).add(emptyToken);
        }

        return firstTokenSets;
    }

    private static void findAllFirstElements(
        Set<ProductionRule> productionRules, 
        HashMap<NonTerminal, Set<Token>> firstTokenSets,
        HashMap<NonTerminal, Set<NonTerminal>> firstNTSets, 
        Token emptyToken,
        Set<NonTerminal> emptyNonTerminals
    ) {
        for(ProductionRule rule : productionRules) {
            if(rule.productionSequence().length == 0 || 
                (rule.productionSequence().length == 1 && rule.getFirstElement().equals(emptyToken))
            ) {
                emptyNonTerminals.add(rule.nonTerminal());

                Set<LexicalElement> followingElements = getFollowingElements(rule.nonTerminal(), productionRules);

                for (LexicalElement element : followingElements) {
                    if(element instanceof Token) {
                        firstTokenSets.get(rule.nonTerminal()).add((Token)element);
                    }
                    else {
                        firstNTSets.get(rule.nonTerminal()).add((NonTerminal)element);
                    }
                }

                continue;
            }

            boolean keepChecking = true;
            int index = 0;
            LexicalElement element = rule.getFirstElement();

            while(keepChecking) {
                if(element instanceof Token) {
                    if(emptyToken.equals(rule.getFirstElement())) {
                        element = rule.productionSequence()[index];
                    }

                    keepChecking = false;
                    firstTokenSets.get(rule.nonTerminal()).add((Token)rule.getFirstElement());
                }
                else {
                    keepChecking = false;

                    if(rule.nonTerminal().equals(rule.getFirstElement())) { break; }

                    firstNTSets.get(rule.nonTerminal()).add((NonTerminal)rule.getFirstElement());
                }
            }
        }
    }

    private static void resolveCompleteSets(HashMap<NonTerminal, Set<Token>> firstTokenSets,
            HashMap<NonTerminal, Set<NonTerminal>> firstNTSets) {
        List<NonTerminal> completeSets = new ArrayList<>();

        do {
            completeSets.clear();

            for(NonTerminal nonTerminal : firstNTSets.keySet()) {
                if(firstNTSets.get(nonTerminal).isEmpty()) {
                    completeSets.add(nonTerminal);
                }
            }

            // Replace completed sets with their tokens
            for(NonTerminal nonTerminal : completeSets) {
                firstNTSets.remove(nonTerminal);

                for(NonTerminal nonTerminal2 : firstNTSets.keySet()) {
                    Set<NonTerminal> elements = firstNTSets.get(nonTerminal2);

                    for(NonTerminal element : elements) {
                        if(nonTerminal.equals(element)) {
                            elements.remove(nonTerminal);

                            firstTokenSets.get(nonTerminal2).addAll(
                                firstTokenSets.get(nonTerminal)
                            );

                            break;
                        }
                    }
                }
            }
        } while (!completeSets.isEmpty());
    }

    private static void resolveConnectedSets(HashMap<NonTerminal, Set<Token>> firstTokenSets,
            HashMap<NonTerminal, Set<NonTerminal>> firstNTSets) {
        boolean changesMade = false;

        do {
            changesMade = false;

            for (NonTerminal nonTerminal : firstNTSets.keySet()) {
                for (NonTerminal nonTerminal2 : firstNTSets.get(nonTerminal)) {
                    if(firstTokenSets.get(nonTerminal).containsAll(firstTokenSets.get(nonTerminal2))) {
                        continue;
                    }

                    firstTokenSets.get(nonTerminal).addAll(
                        firstTokenSets.get(nonTerminal2)
                    );
                    
                    changesMade = true;
                }
            }
        } while (changesMade);
    }

    private static Set<LexicalElement> getFollowingElements(NonTerminal nonTerminal, Set<ProductionRule> productionRules) {
        Set<LexicalElement> followingElements = new HashSet<>();

        for(ProductionRule rule : productionRules) {
            LexicalElement[] elements = rule.productionSequence();

            for (int i = 0; i < elements.length - 1; i++) {
                if(!nonTerminal.equals(elements[i])) { continue; }

                LexicalElement nextElement = elements[i + 1];

                if(!nonTerminal.equals(nextElement)) {
                    followingElements.add(nextElement);
                }
            }
        }

        return followingElements;
    }

}
