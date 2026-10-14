package helper_objects;

import java.util.*;

import grammar_objects.*;

public class FirstSetGenerator {
    private static Token EmptyToken = new EmptyToken();

    public static HashMap<NonTerminal, Set<Token>> generate(Set<ProductionRule> productionRules, Set<NonTerminal> nonTerminals) {
        HashMap<NonTerminal, Set<Token>> firstTokenSets = new HashMap<>();
        HashMap<NonTerminal, Set<NonTerminal>> firstNTSets = new HashMap<>();

        for(NonTerminal nonTerminal : nonTerminals) {
            firstTokenSets.put(nonTerminal, new HashSet<>());
            firstNTSets.put(nonTerminal, new HashSet<>());
        }

        Set<NonTerminal> emptyNonTerminals = findEmptyNonTerminals(productionRules, nonTerminals);

        findAllFirstElements(productionRules, firstTokenSets, firstNTSets, emptyNonTerminals);

        resolveCompleteSets(firstTokenSets, firstNTSets);
        resolveConnectedSets(firstTokenSets, firstNTSets);

        return firstTokenSets;
    }

    private static Set<NonTerminal> findEmptyNonTerminals(Set<ProductionRule> productionRules, Set<NonTerminal> nonTerminals) {
        Set<NonTerminal> emptyNonTerminals = new HashSet<>();

        for (ProductionRule rule : productionRules) {
            if (rule.productionSequence().length == 0) {
                emptyNonTerminals.add(rule.nonTerminal());
                continue;
            }

            boolean isEmpty = true;

            for (LexicalElement element : rule.productionSequence()) {
                if (!EmptyToken.equals(element)) {
                    isEmpty = false;
                    break;
                }
            }

            if (isEmpty) {
                emptyNonTerminals.add(rule.nonTerminal());
            }
        }

        return emptyNonTerminals;
    }

    private static void findAllFirstElements(
        Set<ProductionRule> productionRules, 
        HashMap<NonTerminal, Set<Token>> firstTokenSets,
        HashMap<NonTerminal, Set<NonTerminal>> firstNTSets,
        Set<NonTerminal> emptyNonTerminals
    ) {
        for(ProductionRule rule : productionRules) {
            findFirstElementsForRule(
                rule.nonTerminal(),
                rule.productionSequence(), 
                firstTokenSets,
                firstNTSets,
                emptyNonTerminals
            );
        }
    }

    private static void findFirstElementsForRule(
        NonTerminal ruleNonTerminal,
        LexicalElement[] productionSequence, 
        HashMap<NonTerminal, Set<Token>> firstTokenSets,
        HashMap<NonTerminal, Set<NonTerminal>> firstNTSets,
        Set<NonTerminal> emptyNonTerminals
    ) {
        boolean isEmpty = true;

        for(LexicalElement element : productionSequence) {
            if(element instanceof Token) {
                if(EmptyToken.equals(element)) {
                    firstTokenSets.get(ruleNonTerminal).add(EmptyToken);
                    continue;
                }

                firstTokenSets.get(ruleNonTerminal).add((Token)element);
                return;
            }
            else {
                if(ruleNonTerminal.equals(element)) {
                    if (emptyNonTerminals.contains((NonTerminal)element)) {
                        continue;
                    }

                    isEmpty = false;
                    break;
                }

                firstNTSets.get(ruleNonTerminal).add((NonTerminal)element);

                if(!emptyNonTerminals.contains((NonTerminal)element)) { 
                    isEmpty = false;
                    return;
                }
            }
        }

        if(isEmpty) {
            firstTokenSets.get(ruleNonTerminal).add(EmptyToken);
        }
    }

    private static void resolveCompleteSets(
        HashMap<NonTerminal, Set<Token>> firstTokenSets,
        HashMap<NonTerminal, Set<NonTerminal>> firstNTSets
    ) {
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
                        if(!nonTerminal.equals(element)) { continue; }
                        
                        elements.remove(nonTerminal);

                        firstTokenSets.get(nonTerminal2).addAll(
                            firstTokenSets.get(nonTerminal)
                        );

                        break;
                    }
                }
            }
        } while (!completeSets.isEmpty());
    }

    private static void resolveConnectedSets(
        HashMap<NonTerminal, Set<Token>> firstTokenSets,
        HashMap<NonTerminal, Set<NonTerminal>> firstNTSets
    ) {
        boolean changesMade = false;

        do {
            changesMade = false;

            for (NonTerminal nonTerminal : firstNTSets.keySet()) {
                for (NonTerminal nonTerminal2 : firstNTSets.get(nonTerminal)) {
                    Set<Token> tokensWithoutEmpty = removeEmpty(firstTokenSets.get(nonTerminal2));

                    if(firstTokenSets.get(nonTerminal).containsAll(tokensWithoutEmpty)) {
                        continue;
                    }

                    firstTokenSets.get(nonTerminal).addAll(tokensWithoutEmpty);
                    
                    changesMade = true;
                }
            }
        } while (changesMade);
    }

    private static Set<Token> removeEmpty(Set<Token> set) {
        Set<Token> tokens = new HashSet<>();

        for (Token token : set) {
            if(!EmptyToken.equals(token)) {
                tokens.add(token);
            }
        }
        
        return tokens;
    }

}
