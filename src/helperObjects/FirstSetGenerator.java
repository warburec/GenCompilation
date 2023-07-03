package helperObjects;

import java.util.*;

import grammar_objects.*;

public class FirstSetGenerator {

    public static HashMap<NonTerminal, Set<Token>> generate(Set<ProductionRule> productionRules, Set<NonTerminal> nonTerminals) {
        HashMap<NonTerminal, Set<Token>> firstTokenSets = new HashMap<>();
        HashMap<NonTerminal, Set<NonTerminal>> firstNTSets = new HashMap<>();

        Token emptyToken = new Token("");

        for(NonTerminal nonTerminal : nonTerminals) {
            firstTokenSets.put(nonTerminal, new HashSet<>());
            firstNTSets.put(nonTerminal, new HashSet<>());
        }

        Set<NonTerminal> emptyNonTerminals = findEmptyNonTerminals(productionRules, nonTerminals, emptyToken);

        findAllFirstElements(productionRules, firstTokenSets, firstNTSets, emptyToken, emptyNonTerminals);

        resolveCompleteSets(firstTokenSets, firstNTSets);
        resolveConnectedSets(firstTokenSets, firstNTSets, emptyToken);

        for (NonTerminal nonTerminal : emptyNonTerminals) {
            firstTokenSets.get(nonTerminal).add(emptyToken);
        }

        return firstTokenSets;
    }

    private static Set<NonTerminal> findEmptyNonTerminals(Set<ProductionRule> productionRules, Set<NonTerminal> nonTerminals, Token emptyToken) {
        Set<NonTerminal> emptyNonTerminals = new HashSet<>();

        for (ProductionRule rule : productionRules) {
            if(rule.productionSequence().length == 0 || 
                (rule.productionSequence().length == 1 && rule.getFirstElement().equals(emptyToken))
            ) {
                emptyNonTerminals.add(rule.nonTerminal());
            }
        }

        return emptyNonTerminals;
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
                firstTokenSets.get(rule.nonTerminal()).add((Token)emptyToken);
                continue;
            }

            boolean foundNonEmpty = false;
            int index = -1;
            LexicalElement element = rule.getFirstElement();

            while(index < rule.productionSequence().length) {
                index += 1;
                element = rule.productionSequence()[index];

                if(element instanceof Token) {
                    if(emptyToken.equals(element)) {
                        continue;
                    }

                    firstTokenSets.get(rule.nonTerminal()).add((Token)element);

                    foundNonEmpty = true;
                    break;
                }
                else {
                    if(rule.nonTerminal().equals(element)) { 
                        foundNonEmpty = true;
                        break;
                    }

                    firstNTSets.get(rule.nonTerminal()).add((NonTerminal)element);

                    if(!emptyNonTerminals.contains((NonTerminal)element)) { 
                        foundNonEmpty = true;
                        break;
                    }
                }
            }

            if(!foundNonEmpty) {
                if(emptyNonTerminals.contains((NonTerminal)element)) {
                    firstTokenSets.get(rule.nonTerminal()).add(emptyToken);
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

    private static void resolveConnectedSets(
        HashMap<NonTerminal, Set<Token>> firstTokenSets,
        HashMap<NonTerminal, Set<NonTerminal>> firstNTSets,
        Token emptyToken
    ) {
        boolean changesMade = false;

        do {
            changesMade = false;

            for (NonTerminal nonTerminal : firstNTSets.keySet()) {
                for (NonTerminal nonTerminal2 : firstNTSets.get(nonTerminal)) {
                    Set<Token> tokensWithoutEmpty = removeEmpty(firstTokenSets.get(nonTerminal2), emptyToken);

                    if(firstTokenSets.get(nonTerminal).containsAll(tokensWithoutEmpty)) {
                        continue;
                    }

                    firstTokenSets.get(nonTerminal).addAll(tokensWithoutEmpty);
                    
                    changesMade = true;
                }
            }
        } while (changesMade);
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
