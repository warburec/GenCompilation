package test_aids.test_grammars.basic_identifier;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import grammar_objects.*;
import grammars.basic_identifier.BasicIdentifierGrammar;
import grammars.basic_identifier.convertors.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;
import test_aids.*;
import test_aids.TestGrammarBuilder.TableGatherer;
import test_aids.test_grammars.*;

/**
 * <statement list> := <statement> |
 *                     <statement list> <statement>
 * <statement> := identifier = <element> + <element>;
 * <element> := identifier | number
 */
public class BasicIdentifierTestGrammar {

    private Grammar grammar = BasicIdentifierGrammar.produce();
    private EOF eof;
    private List<State> states;
    private List<Token> allTokens;

    public TestGrammar getGrammar(GrammarType grammarType) {
        TestGrammarBuilder builder = new TestGrammarBuilder(grammar);

        eof = builder.endOfFile;
        states = gatherStates(builder.extraRootRule);

        allTokens = new ArrayList<>();
        allTokens.addAll(grammar.getParts().tokens());
        allTokens.add(eof);

        TableGatherer tableGatherer = builder.setUp()
        .addStates(states)
        .selectState(states.get(0))
            .addBranch(new Route(states.get(1), new NonTerminal("statement list")))
            .addBranch(new Route(states.get(10), new NonTerminal("statement")))
            .addBranch(new Route(states.get(3), new Identifier("identifier")))
            .deselectState()
        
        .selectState(states.get(1))
            .addBranch(new Route(states.get(2), new NonTerminal("statement")))
            .addBranch(new Route(states.get(3), new Identifier("identifier")))
            .deselectState()
        
        .selectState(states.get(3))
            .addBranch(new Route(states.get(4), new Token("=")))
            .deselectState()
        
        .selectState(states.get(4))
            .addBranch(new Route(states.get(5), new NonTerminal("element")))
            .addBranch(new Route(states.get(8), new Identifier("identifier")))
            .addBranch(new Route(states.get(9), new Literal("number")))
            .deselectState()
        
        .selectState(states.get(5))
            .addBranch(new Route(states.get(6), new Token("+")))
            .deselectState()
        
        .selectState(states.get(6))
            .addBranch(new Route(states.get(7), new NonTerminal("element")))
            .addBranch(new Route(states.get(8), new Identifier("identifier")))
            .addBranch(new Route(states.get(9), new Literal("number")))
            .deselectState()
        
        .selectState(states.get(7))
            .addBranch(new Route(states.get(11), new Token(";")))
            .deselectState()
        
        .commitStates();

        tableGatherer = setUpActionTable(grammarType, tableGatherer);

        return tableGatherer
        .selectState(states.get(0))
            .addGoto(new NonTerminal("statement list"), states.get(1))
            .addGoto(new NonTerminal("statement"), states.get(10))
            .deselectState()

        .selectState(states.get(1))
            .addGoto(new NonTerminal("statement"), states.get(2))
            .deselectState()

        .selectState(states.get(4))
            .addGoto(new NonTerminal("element"), states.get(5))
            .deselectState()

        .selectState(states.get(6))
            .addGoto(new NonTerminal("element"), states.get(7))
            .deselectState()
        
        .commitTables()

        .setParseTreeRoot("XToYToX", parseTree0())
        .setParseTreeRoot("XToYToXSemantic", parseTree0())

        .commitParseTrees()

        .setRuleConvertor("Java XToYToX", XToYToX.produce())
        .setRuleConvertor("Java XToYToXSemantic", XToYToXSemantic.produce())

        .commitRuleConvertors()

        .setCodeGeneration("Java XToYToX",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tx = 1 + 2;\n" +
            "\t\ty = x + 3;\n" +
            "\t\tx = y + 0;\n" +
            "\t\tSystem.out.println(x);\n" +
            "\t}\n" +
            "}"
        )
        .setCodeGeneration("Java XToYToXSemantic",
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tint x = 1 + 2;\n" +
            "\t\tint y = x + 3;\n" +
            "\t\tx = y + 0;\n" +
            "\t\tSystem.out.println(x);\n" +
            "\t}\n" +
            "}"
        )

        .commitCodeGenerations()
        .generateTestGrammar();
    }

    private List<State> gatherStates(ProductionRule extraRootRule) {
        states = new ArrayList<State>();

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
            states.get(0)
        ));

        states.add(new State( //2
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 2),
            }),
            states.get(1)
        ));

        states.add(new State( //3
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 1),
            }),
            states.get(1)
        ));

        states.add(new State( //4
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 2),
                new GrammarPosition(getRule(3), 0),
                new GrammarPosition(getRule(4), 0)
            }),
            states.get(3)
        ));

        states.add(new State( //5
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 3)
            }),
            states.get(4)
        ));

        states.add(new State( //6
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 4),
                new GrammarPosition(getRule(3), 0),
                new GrammarPosition(getRule(4), 0)
            }),
            states.get(5)
        ));

        states.add(new State( //7
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 5)
            }),
            states.get(6)
        ));

        states.add(new State( //8
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(3), 1)
            }),
            states.get(6)
        ));

        states.add(new State( //9
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(4), 1)
            }),
            states.get(6)
        ));

        states.add(new State( //10
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 1)
            }),
            states.get(0)
        ));

        states.add(new State( //11
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 6)
            }),
            states.get(7)
        ));

        return states;
    }
    
    private TableGatherer setUpActionTable(GrammarType type, TableGatherer tableGatherer) {
        switch (type) {
            case LR0 -> lr0ActionTable(tableGatherer);
            case SLR1 -> slr1ActionTable(tableGatherer);
            
            default -> throw new UnsupportedGrammarException(type);
        }

        return tableGatherer;
    }
    
    private void lr0ActionTable(TableGatherer tableGatherer) {
        tableGatherer
        .selectState(states.get(0))
            .addAction(new Identifier("identifier"), new Shift(states.get(3)))
            .deselectState()

        .selectState(states.get(1))
            .addAction(new Identifier("identifier"), new Shift(states.get(3)))
            .addAction(eof, new Accept())
            .deselectState()

        .selectState(states.get(2))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(1))
            )))
            .deselectState()

        .selectState(states.get(3))
            .addAction(new Token("="), new Shift(states.get(4)))
            .deselectState()

        .selectState(states.get(4))
            .addAction(new Identifier("identifier"), new Shift(states.get(8)))
            .addAction(new Literal("number"), new Shift(states.get(9)))
            .deselectState()

        .selectState(states.get(5))
            .addAction(new Token("+"), new Shift(states.get(6)))
            .deselectState()

        .selectState(states.get(6))
            .addAction(new Identifier("identifier"), new Shift(states.get(8)))
            .addAction(new Literal("number"), new Shift(states.get(9)))
            .deselectState()

        .selectState(states.get(7))
            .addAction(new Token(";"), new Shift( states.get(11)))
            .deselectState()

        .selectState(states.get(8))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(3))
            )))
            .deselectState()

        .selectState(states.get(9))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(4))
            )))
            .deselectState()

        .selectState(states.get(10))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(0))
            )))
            .deselectState()

        .selectState(states.get(11))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(2))
            )))
            .deselectState();
    }

    private void slr1ActionTable(TableGatherer tableGatherer) {
        tableGatherer
        .selectState(states.get(0))
            .addAction(new Identifier("identifier"), new Shift(states.get(3)))
            .deselectState()

        .selectState(states.get(1))
            .addAction(new Identifier("identifier"), new Shift(states.get(3)))
            .addAction(eof, new Accept())
            .deselectState()

        .selectState(states.get(2))
            .addAction(new Identifier("identifier"), new Reduction(getRule(1)))
            .addAction(eof, new Reduction(getRule(1)))
            .deselectState()

        .selectState(states.get(3))
            .addAction(new Token("="), new Shift(states.get(4)))
            .deselectState()

        .selectState(states.get(4))
            .addAction(new Identifier("identifier"), new Shift(states.get(8)))
            .addAction(new Literal("number"), new Shift(states.get(9)))
            .deselectState()

        .selectState(states.get(5))
            .addAction(new Token("+"), new Shift(states.get(6)))
            .deselectState()

        .selectState(states.get(6))
            .addAction(new Identifier("identifier"), new Shift(states.get(8)))
            .addAction(new Literal("number"), new Shift(states.get(9)))
            .deselectState()

        .selectState(states.get(7))
            .addAction(new Token(";"),new Shift( states.get(11)))
            .deselectState()

        .selectState(states.get(8))
            .addAction(new Token("+"), new Reduction(getRule(3)))
            .addAction(new Token(";"), new Reduction(getRule(3)))
            .deselectState()

        .selectState(states.get(9))
            .addAction(new Token("+"), new Reduction(getRule(4)))
            .addAction(new Token(";"), new Reduction(getRule(4)))
            .deselectState()

        .selectState(states.get(10))
            .addAction(new Identifier("identifier"), new Reduction(getRule(0)))
            .addAction(eof, new Reduction(getRule(0)))
            .deselectState()

        .selectState(states.get(11))
            .addAction(new Identifier("identifier"), new Reduction(getRule(2)))
            .addAction(eof, new Reduction(getRule(2)))
            .deselectState();
    }

    /**
     * Parse tree for the sentence "XToYToX"
     * @return The root ParseState of the tree
     */
    private ParseState parseTree0() {
        List<ParseState> parseStates = new ArrayList<>();

        parseStates.add(new ShiftedState(
            states.get(3), 
            new Identifier("identifier", "x")));

        parseStates.add(new ShiftedState(
            states.get(4), 
            new Token("=")));
        
        parseStates.add(new ShiftedState(
            states.get(9), 
            new Literal("number", "1")));
        
        parseStates.add(new ShiftedState(
            states.get(6), 
            new Token("+")));
        
        parseStates.add(new ShiftedState(
            states.get(9), 
            new Literal("number", "2")));
        
        parseStates.add(new ShiftedState(
            states.get(11), 
            new Token(";")));
        
        parseStates.add(new ShiftedState(
            states.get(3), 
            new Identifier("identifier", "y")));
        
        parseStates.add(new ShiftedState(
            states.get(4), 
            new Token("=")));
        
        parseStates.add(new ShiftedState(
            states.get(8), 
            new Identifier("identifier", "x")));
        
        parseStates.add(new ShiftedState(
            states.get(6), 
            new Token("+")));
        
        parseStates.add(new ShiftedState(
            states.get(9), 
            new Literal("number", "3")));
        
        parseStates.add(new ShiftedState(
            states.get(11), 
            new Token(";")));
        
        parseStates.add(new ShiftedState(
            states.get(3), 
            new Identifier("identifier", "x")));
        
        parseStates.add(new ShiftedState(
            states.get(4), 
            new Token("=")));
        
        parseStates.add(new ShiftedState(
            states.get(8), 
            new Identifier("identifier", "y")));
        
        parseStates.add(new ShiftedState(
            states.get(6), 
            new Token("+")));
        
        parseStates.add(new ShiftedState(
            states.get(9), 
            new Literal("number", "0")));
        
        parseStates.add(new ShiftedState(
            states.get(11), 
            new Token(";")));
            

        parseStates.add(new ReducedState(
            states.get(5), 
            getRule(4), 
            Arrays.asList(new ParseState[] {
                parseStates.get(2)
            })));
        
        parseStates.add(new ReducedState(
            states.get(7), 
            getRule(4), 
            Arrays.asList(new ParseState[] {
                parseStates.get(4)
            })));
        
        parseStates.add(new ReducedState(
            states.get(5), 
            getRule(3), 
            Arrays.asList(new ParseState[] {
                parseStates.get(8)
            })));
        
        parseStates.add(new ReducedState(
            states.get(7), 
            getRule(4),
            Arrays.asList(new ParseState[] {
                parseStates.get(10)
            })));
        
        parseStates.add(new ReducedState(
            states.get(5), 
            getRule(3), 
            Arrays.asList(new ParseState[] {
                parseStates.get(14)
            })));
        
        parseStates.add(new ReducedState(
            states.get(7), 
            getRule(4),
            Arrays.asList(new ParseState[] {
                parseStates.get(16)
            })));

        
        parseStates.add(new ReducedState(
            states.get(10), 
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
            states.get(2), 
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
            states.get(2), 
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
            states.get(1), 
            getRule(0), 
            Arrays.asList(new ParseState[] {
                parseStates.get(24)
            })));

        
        parseStates.add(new ReducedState(
            states.get(1), 
            getRule(1), 
            Arrays.asList(new ParseState[] {
                parseStates.get(27),
                parseStates.get(25)
            })));
        
        parseStates.add(new ReducedState(
            states.get(1), 
            getRule(1), 
            Arrays.asList(new ParseState[] {
                parseStates.get(28),
                parseStates.get(26)
            })));
        
        return parseStates.get(parseStates.size() - 1);
    }

    private ProductionRule getRule(int index) {
        return grammar.getRule(index);
    }

}