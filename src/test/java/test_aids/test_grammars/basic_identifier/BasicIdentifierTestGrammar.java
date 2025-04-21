package test_aids.test_grammars.basic_identifier;

import java.util.*;

import grammar_objects.*;
import grammars.basic_identifier.BasicIdentifierGrammar;
import grammars.basic_identifier.convertors.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;
import test_aids.*;
import test_aids.test_grammars.*;

/**
 * <statement list> := <statement> |
 *                     <statement list> <statement>
 * <statement> := identifier = <element> + <element>;
 * <element> := identifier | number
 */
public class BasicIdentifierTestGrammar extends TestGrammar {

    public BasicIdentifierTestGrammar(GrammarType type) {
        super(type);
    }

    @Override
    protected Grammar setUpGrammar(GrammarType type) {
        return BasicIdentifierGrammar.produce();
    }

    @Override
    protected void setUpStates(GrammarType type, List<State> states, ProductionRule extraRootRule, Token endOfFile) {
        states.add(new State( //0
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 0),
                new GrammarPosition(getRule(1), 0),
                new GrammarPosition(getRule(0), 0),
                new GrammarPosition(getRule(2), 0)
            }),
            null
        ));

        states.add(new State( //1
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 1),
                new GrammarPosition(getRule(1), 1),
                new GrammarPosition(getRule(2), 0)
            }),
            getState(0)
        ));

        states.add(new State( //2
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 2),
            }),
            getState(1)
        ));

        states.add(new State( //3
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 1),
            }),
            getState(1)
        ));

        states.add(new State( //4
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 2),
                new GrammarPosition(getRule(3), 0),
                new GrammarPosition(getRule(4), 0)
            }),
            getState(3)
        ));

        states.add(new State( //5
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 3)
            }),
            getState(4)
        ));

        states.add(new State( //6
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 4),
                new GrammarPosition(getRule(3), 0),
                new GrammarPosition(getRule(4), 0)
            }),
            getState(5)
        ));

        states.add(new State( //7
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 5)
            }),
            getState(6)
        ));

        states.add(new State( //8
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(3), 1)
            }),
            getState(6)
        ));

        states.add(new State( //9
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(4), 1)
            }),
            getState(6)
        ));

        states.add(new State( //10
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 1)
            }),
            getState(0)
        ));

        states.add(new State( //11
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 6)
            }),
            getState(7)
        ));

        //Branches
        getState(0)
            .addBranch(new Route(getState(1), new NonTerminal("statement list")))
            .addBranch(new Route(getState(10), new NonTerminal("statement")))
            .addBranch(new Route(getState(3), new Identifier("identifier")));
        
        getState(1)
            .addBranch(new Route(getState(2), new NonTerminal("statement")))
            .addBranch(new Route(getState(3), new Identifier("identifier")));
        
        getState(3)
            .addBranch(new Route(getState(4), new Token("=")));
        
        getState(4)
            .addBranch(new Route(getState(5), new NonTerminal("element")))
            .addBranch(new Route(getState(8), new Identifier("identifier")))
            .addBranch(new Route(getState(9), new Literal("number")));
        
        getState(5)
            .addBranch(new Route(getState(6), new Token("+")));
        
        getState(6)
            .addBranch(new Route(getState(7), new NonTerminal("element")))
            .addBranch(new Route(getState(8), new Identifier("identifier")))
            .addBranch(new Route(getState(9), new Literal("number")));
        
        getState(7)
            .addBranch(new Route(getState(11), new Token(";")));
    }
    
    @Override
    protected void setUpActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
        switch (type) {
            case LR0 -> lr0ActionTable(type, actionTable, endOfFile);
            case SLR1 -> slr1ActionTable(type, actionTable, endOfFile);
            
            default -> throw new UnsupportedGrammarException(type);
        }
    }
    
    private void lr0ActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
        List<Token> allTokens = new ArrayList<>();
        allTokens.addAll(grammar.getParts().tokens());
        allTokens.add(endOfFile);

        Map<Token, Action> stateActions = actionTable.get(getState(0));
        stateActions.put(new Identifier("identifier"), new Shift(getState(3)));

        stateActions = actionTable.get(getState(1));
        stateActions.put(new Identifier("identifier"), new Shift(getState(3)));
        stateActions.put(endOfFile, new Accept());

        stateActions = actionTable.get(getState(2));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(1)));
        }

        stateActions = actionTable.get(getState(3));
        stateActions.put(new Token("="), new Shift(getState(4)));

        stateActions = actionTable.get(getState(4));
        stateActions.put(new Identifier("identifier"), new Shift(getState(8)));
        stateActions.put(new Literal("number"), new Shift(getState(9)));

        stateActions = actionTable.get(getState(5));
        stateActions.put(new Token("+"), new Shift(getState(6)));

        stateActions = actionTable.get(getState(6));
        stateActions.put(new Identifier("identifier"), new Shift(getState(8)));
        stateActions.put(new Literal("number"), new Shift(getState(9)));

        stateActions = actionTable.get(getState(7));
        stateActions.put(new Token(";"),new Shift( getState(11)));

        stateActions = actionTable.get(getState(8));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(3)));
        }

        stateActions = actionTable.get(getState(9));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(4)));
        }

        stateActions = actionTable.get(getState(10));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(0)));
        }

        stateActions = actionTable.get(getState(11));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(2)));
        }
    }

    private void slr1ActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
        Map<Token, Action> stateActions = actionTable.get(getState(0));
        stateActions.put(new Identifier("identifier"), new Shift(getState(3)));

        stateActions = actionTable.get(getState(1));
        stateActions.put(new Identifier("identifier"), new Shift(getState(3)));
        stateActions.put(endOfFile, new Accept());

        stateActions = actionTable.get(getState(2));
        stateActions.put(new Identifier("identifier"), new Reduction(getRule(1)));
        stateActions.put(endOfFile, new Reduction(getRule(1)));

        stateActions = actionTable.get(getState(3));
        stateActions.put(new Token("="), new Shift(getState(4)));

        stateActions = actionTable.get(getState(4));
        stateActions.put(new Identifier("identifier"), new Shift(getState(8)));
        stateActions.put(new Literal("number"), new Shift(getState(9)));

        stateActions = actionTable.get(getState(5));
        stateActions.put(new Token("+"), new Shift(getState(6)));

        stateActions = actionTable.get(getState(6));
        stateActions.put(new Identifier("identifier"), new Shift(getState(8)));
        stateActions.put(new Literal("number"), new Shift(getState(9)));

        stateActions = actionTable.get(getState(7));
        stateActions.put(new Token(";"),new Shift( getState(11)));

        stateActions = actionTable.get(getState(8));
        stateActions.put(new Token("+"), new Reduction(getRule(3)));
        stateActions.put(new Token(";"), new Reduction(getRule(3)));

        stateActions = actionTable.get(getState(9));
        stateActions.put(new Token("+"), new Reduction(getRule(4)));
        stateActions.put(new Token(";"), new Reduction(getRule(4)));

        stateActions = actionTable.get(getState(10));
        stateActions.put(new Identifier("identifier"), new Reduction(getRule(0)));
        stateActions.put(endOfFile, new Reduction(getRule(0)));

        stateActions = actionTable.get(getState(11));
        stateActions.put(new Identifier("identifier"), new Reduction(getRule(2)));
        stateActions.put(endOfFile, new Reduction(getRule(2)));
    }

    @Override
    protected void setUpGotoTable(GrammarType type, Map<State, Map<NonTerminal, State>> gotoTable) {
        Map<NonTerminal, State> currentGotoActions = new HashMap<>();
        
        currentGotoActions.put(new NonTerminal("statement list"), getState(1));
        currentGotoActions.put(new NonTerminal("statement"), getState(10));
        gotoTable.put(getState(0), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("statement"), getState(2));
        gotoTable.put(getState(1), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("element"), getState(5));
        gotoTable.put(getState(4), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("element"), getState(7));
        gotoTable.put(getState(6), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();
    }

    @Override
    protected void setUpParseTrees(Map<String, ParseTreeBuilder> parseRootMap) {
        parseRootMap.put("XToYToX", () -> parseTree0());
        parseRootMap.put("XToYToXSemantic", () -> parseTree0());
    }

    /**
     * Parse tree for the sentence "XToYToX"
     * @return The root ParseState of the tree
     */
    private ParseState parseTree0() {
        List<ParseState> parseStates = new ArrayList<>();

        parseStates.add(new ShiftedState(
            getState(3), 
            new Identifier("identifier", "x")));

        parseStates.add(new ShiftedState(
            getState(4), 
            new Token("=")));
        
        parseStates.add(new ShiftedState(
            getState(9), 
            new Literal("number", "1")));
        
        parseStates.add(new ShiftedState(
            getState(6), 
            new Token("+")));
        
        parseStates.add(new ShiftedState(
            getState(9), 
            new Literal("number", "2")));
        
        parseStates.add(new ShiftedState(
            getState(11), 
            new Token(";")));
        
        parseStates.add(new ShiftedState(
            getState(3), 
            new Identifier("identifier", "y")));
        
        parseStates.add(new ShiftedState(
            getState(4), 
            new Token("=")));
        
        parseStates.add(new ShiftedState(
            getState(8), 
            new Identifier("identifier", "x")));
        
        parseStates.add(new ShiftedState(
            getState(6), 
            new Token("+")));
        
        parseStates.add(new ShiftedState(
            getState(9), 
            new Literal("number", "3")));
        
        parseStates.add(new ShiftedState(
            getState(11), 
            new Token(";")));
        
        parseStates.add(new ShiftedState(
            getState(3), 
            new Identifier("identifier", "x")));
        
        parseStates.add(new ShiftedState(
            getState(4), 
            new Token("=")));
        
        parseStates.add(new ShiftedState(
            getState(8), 
            new Identifier("identifier", "y")));
        
        parseStates.add(new ShiftedState(
            getState(6), 
            new Token("+")));
        
        parseStates.add(new ShiftedState(
            getState(9), 
            new Literal("number", "0")));
        
        parseStates.add(new ShiftedState(
            getState(11), 
            new Token(";")));
            

        parseStates.add(new ReducedState(
            getState(5), 
            getRule(4), 
            Arrays.asList(new ParseState[] {
                parseStates.get(2)
            })));
        
        parseStates.add(new ReducedState(
            getState(7), 
            getRule(4), 
            Arrays.asList(new ParseState[] {
                parseStates.get(4)
            })));
        
        parseStates.add(new ReducedState(
            getState(5), 
            getRule(3), 
            Arrays.asList(new ParseState[] {
                parseStates.get(8)
            })));
        
        parseStates.add(new ReducedState(
            getState(7), 
            getRule(4),
            Arrays.asList(new ParseState[] {
                parseStates.get(10)
            })));
        
        parseStates.add(new ReducedState(
            getState(5), 
            getRule(3), 
            Arrays.asList(new ParseState[] {
                parseStates.get(14)
            })));
        
        parseStates.add(new ReducedState(
            getState(7), 
            getRule(4),
            Arrays.asList(new ParseState[] {
                parseStates.get(16)
            })));

        
        parseStates.add(new ReducedState(
            getState(10), 
            getRule(2), 
            Arrays.asList(new ParseState[] {
                parseStates.get(0),
                parseStates.get(1),
                parseStates.get(18),
                parseStates.get(3),
                parseStates.get(19),
                parseStates.get(5)
            })));
        
        parseStates.add(new ReducedState(
            getState(2), 
            getRule(2), 
            Arrays.asList(new ParseState[] {
                parseStates.get(6),
                parseStates.get(7),
                parseStates.get(20),
                parseStates.get(9),
                parseStates.get(21),
                parseStates.get(11)
            })));
        
        parseStates.add(new ReducedState(
            getState(2), 
            getRule(2), 
            Arrays.asList(new ParseState[] {
                parseStates.get(12),
                parseStates.get(13),
                parseStates.get(22),
                parseStates.get(15),
                parseStates.get(23),
                parseStates.get(17)
            })));
        
        
        parseStates.add(new ReducedState(
            getState(1), 
            getRule(0), 
            Arrays.asList(new ParseState[] {
                parseStates.get(24)
            })));

        
        parseStates.add(new ReducedState(
            getState(1), 
            getRule(1), 
            Arrays.asList(new ParseState[] {
                parseStates.get(27),
                parseStates.get(25)
            })));
        
        parseStates.add(new ReducedState(
            getState(1), 
            getRule(1), 
            Arrays.asList(new ParseState[] {
                parseStates.get(28),
                parseStates.get(26)
            })));
        
        return parseStates.get(parseStates.size() - 1);
    }

    @Override
    protected void setUpRuleConvertors(GrammarType type, Map<String, Map<String, RuleConvertor>> ruleConvertorMap) {
        ruleConvertorMap.put("Java", new HashMap<>());

        ruleConvertorMap.get("Java").put("XToYToX", XToYToX.produce());
        ruleConvertorMap.get("Java").put("XToYToXSemantic", XToYToXSemantic.produce());
    }
    
    @Override
    protected void setUpCodeGenerations(GrammarType type, Map<String, Map<String, String>> codeGenerations) {
        codeGenerations.put("Java", new HashMap<>());

        codeGenerations.get("Java").put("XToYToX",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tx = 1 + 2;\n" +
            "\t\ty = x + 3;\n" +
            "\t\tx = y + 0;\n" +
            "\t\tSystem.out.println(x);\n" +
            "\t}\n" +
            "}"
        );

        codeGenerations.get("Java").put("XToYToXSemantic",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tint x = 1 + 2;\n" +
            "\t\tint y = x + 3;\n" +
            "\t\tx = y + 0;\n" +
            "\t\tSystem.out.println(x);\n" +
            "\t}\n" +
            "}"
        );
    }

}