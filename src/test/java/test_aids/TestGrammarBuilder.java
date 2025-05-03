package test_aids;

import java.util.*;

import component_construction.builders.grammar_objects.GrammarBuilder;
import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.ParseState;

public class TestGrammarBuilder extends GrammarBuilder {

    protected final TestGrammarBuilder builder;

    // Helper properties
    public final ProductionRule extraRootRule;
    public final EOF endOfFile = new EOF();

    protected Grammar grammar;
    protected List<State> states = new ArrayList<>();
    protected Map<State, Map<Token, Action>> actionTable = new HashMap<>();
    protected Map<State, Map<NonTerminal, State>> gotoTable = new HashMap<>();
    protected RuleConvertor ruleConvertor;
    protected String codeGeneration;
    protected ParseState parseRoot;


    public TestGrammarBuilder(Grammar grammar) {
        this.grammar = grammar;
        extraRootRule = new ProductionRule(null, new LexicalElement[] { grammar.getParts().sentinal() });
        builder = this;
    }

    public StateGatherer setUpStates() {
        return new StateGatherer();
    }

    private void buildTables() {
        for (State state : states) {
            actionTable.put(state, new HashMap<>());
            gotoTable.put(state, new HashMap<>());
        }
    }

    public class StateGatherer {

        public StateGatherer addStates(List<State> states) {
            builder.states.addAll(states);
            return this;
        }

        public StateGatherer addState(State state) {
            builder.states.add(state);
            return this;
        }

        public TableGatherer commitStates() {
            buildTables();
            return new TableGatherer();
        }

    }

    public class TableGatherer {

        public SelectedTableGatherer selectState(State state) {
            return new SelectedTableGatherer(this, state);
        }

        public ParseTreeGatherer commitTables() {
            return new ParseTreeGatherer();
        }

        public class SelectedTableGatherer {

            TableGatherer tableGatherer;
            State selectedState;

            public SelectedTableGatherer(TableGatherer tableGatherer, State state) {
                this.tableGatherer = tableGatherer;
                this.selectedState = state;
            }

            public SelectedTableGatherer addAction(Token token, Action action) {
                builder.actionTable.get(selectedState).put(token, action);
                return this;
            }

            public SelectedTableGatherer addGoto(NonTerminal nonTerminal, State gotoState) {
                builder.gotoTable.get(selectedState).put(nonTerminal, gotoState);
                return this;
            }

            public TableGatherer deselectState() {
                return tableGatherer;
            }
        }
        
    }

    public class ParseTreeGatherer {

        protected RuleConvertorGatherer setParseTreeRoot(ParseState parseRoot) {
            builder.parseRoot = parseRoot;
            return new RuleConvertorGatherer();
        }

    }

    public class RuleConvertorGatherer {

        protected void setRuleConvertor(RuleConvertor ruleConvertor) {
            builder.ruleConvertor = ruleConvertor;
        }

    }

    public class CodeGenerationGatherer {

        protected TestGrammarFinalizer setGeneratedCode(String generatedCode) {
            builder.codeGeneration = generatedCode;
            return new TestGrammarFinalizer();
        }
        
    }

    public class TestGrammarFinalizer {

        public TestGrammar generateTestGrammar() {
            return new TestGrammar(
                tokens, 
                nonTerminals,
                productionRules, 
                sentinal, 
                states, 
                actionTable, 
                gotoTable, 
                ruleConvertor, 
                codeGeneration, 
                parseRoot
            );
        }
        
    }
}
