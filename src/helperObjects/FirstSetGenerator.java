package helperObjects;

import java.util.*;

import grammar_objects.*;

public class FirstSetGenerator {

    public static HashMap<NonTerminal, Set<Token>> generate(Set<ProductionRule> productionRules, Set<NonTerminal> nonTerminals) {
        HashMap<NonTerminal, Set<Token>> firstTokenSets = new HashMap<>();
        HashMap<NonTerminal, Set<NonTerminal>> firstNTSets = new HashMap<>();

        for(NonTerminal nonTerminal : nonTerminals) {
            firstTokenSets.put(nonTerminal, new HashSet<>());
        }

        // Add all first rule elements (including empty token if necessary)
        for(ProductionRule rule : productionRules) {
            if(rule.productionSequence().length == 0) {
                Set<LexicalElement> followingElements = getFollowingElements(rule.nonTerminal(), productionRules);

                for (LexicalElement element : followingElements) {
                    if(element instanceof Token) {
                        firstTokenSets.get(rule.nonTerminal()).add((Token)element);
                    }
                    else {
                        firstNTSets.get(rule.nonTerminal()).add((NonTerminal)element);
                    }
                }
            }

            if(rule.getFirstElement() instanceof Token) {
                firstTokenSets.get(rule.nonTerminal()).add((Token)rule.getFirstElement());
            }
            else {
                if(rule.nonTerminal().equals(rule.getFirstElement())) { continue; }

                firstNTSets.get(rule.nonTerminal()).add((NonTerminal)rule.getFirstElement());
            }            
        }


        List<NonTerminal> completeSets = new ArrayList<>();

        do {
            completeSets.clear();

            for(NonTerminal nonTerminal : nonTerminals) {
                if(firstNTSets.get(nonTerminal).isEmpty()) {
                    completeSets.add(nonTerminal);
                }
            }

            // Replace competed sets with their tokens
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

        boolean changesMade = false;

        do {
            changesMade = false;

            for (NonTerminal nonTerminal : firstNTSets.keySet()) {
                Set<NonTerminal> nTSet = firstNTSets.get(nonTerminal);

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

        return firstTokenSets;
    }

    private static Set<LexicalElement> getFollowingElements(NonTerminal nonTerminal, Set<ProductionRule> productionRules) {
        Set<LexicalElement> followingElements = new HashSet<>();

        for(ProductionRule rule : productionRules) {
            LexicalElement[] elements = rule.productionSequence();

            for (int i = 0; i < elements.length - 1; i++) {
                if(nonTerminal.equals(elements[i++])) { continue; }

                followingElements.add(elements[i++]);
            }
        }

        return followingElements;
    }

}
