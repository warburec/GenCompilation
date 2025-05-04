package test_aids;

import java.util.*;
import java.util.Map.Entry;
import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.ParseState;

//TODO: For all "select" options, ensure the value is held within the builder (or add it if this seems reasonable)
public class TestGrammarBuilder {

    protected final TestGrammarBuilder builder;

    // Helper properties
    public final ProductionRule extraRootRule;
    public final EOF endOfFile = new EOF();

    protected Grammar grammar;
    protected List<State> states = new ArrayList<>();
    protected Map<State, Map<Token, Action>> actionTable = new HashMap<>();
    protected Map<State, Map<NonTerminal, State>> gotoTable = new HashMap<>();
    protected Map<String, RuleConvertor> ruleConvertors = new HashMap<>();
    protected Map<String, String> codeGenerations = new HashMap<>();
    protected Map<String, ParseState> parseRoots = new HashMap<>();

    public TestGrammarBuilder(Grammar grammar) {
        this.grammar = grammar;
        extraRootRule = new ProductionRule(null, new LexicalElement[] { grammar.getParts().sentinal() });
        builder = this;
    }

    public StateGatherer setUp() {
        return new StateGatherer();
    }

    public class StateGatherer {

        public StateGatherer addState(State state) {
            builder.states.add(state);
            return this;
        }

        public StateGatherer addStates(List<State> states) {
            builder.states.addAll(states);
            return this;
        }

        public StateGatherer addBranch(State state, Route route) {
            state.addBranch(route);
            return this;
        }

        public StateGatherer addBranches(State states, Collection<Route> routes) {
            for (Route route : routes) {
                addBranch(states, route);
            }

            return this;
        }

        public SelectedStateGatherer selectState(State state) {
            return new SelectedStateGatherer(this, state);
        }

        public TableGatherer commitStates() {
            return new TableGatherer();
        }

        public class SelectedStateGatherer {

            StateGatherer stateGatherer;
            State selectedState;

            public SelectedStateGatherer(StateGatherer stateGatherer, State state) {
                this.stateGatherer = stateGatherer;
                this.selectedState = state;
            }
    
            public SelectedStateGatherer addBranch(Route route) {
                stateGatherer.addBranch(selectedState, route);
                return this;
            }

            public SelectedStateGatherer addBranches(Collection<Route> routes) {
                stateGatherer.addBranches(selectedState, routes);
                return this;
            }

            public StateGatherer deselectState() {
                return stateGatherer;
            }

        }

    }

    public class TableGatherer {

        public TableGatherer() {
            for (State state : states) {
                builder.actionTable.put(state, new HashMap<>());
                builder.gotoTable.put(state, new HashMap<>());
            }
        }

        public SelectedTableGatherer selectState(State state) {
            return new SelectedTableGatherer(this, state);
        }

        public ParseTreeGatherer commitTables() {
            return new ParseTreeGatherer();
        }

        //TODO: Ensure the right amount of action and goto states if possible
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

            public SelectedTableGatherer addActions(Map<Token, Action> actions) {
                for (Entry<Token, Action> entry : actions.entrySet()) {
                    addAction(entry.getKey(), entry.getValue());
                }

                return this;
            }

            public SelectedTableGatherer addGoto(NonTerminal nonTerminal, State gotoState) {
                builder.gotoTable.get(selectedState).put(nonTerminal, gotoState);
                return this;
            }

            public SelectedTableGatherer addGotos(Map<NonTerminal, State> gotos) {
                for (Entry<NonTerminal, State> entry : gotos.entrySet()) {
                    addGoto(entry.getKey(), entry.getValue());
                }

                return this;
            }

            public TableGatherer deselectState() {
                return tableGatherer;
            }

        }
        
    }

    //TODO: Consider grouping all sentence-wise details by selecting sentence and adding options
    public class ParseTreeGatherer {

        public ParseTreeGatherer setParseTreeRoot(String sentenceName, ParseState parseRoot) {
            builder.parseRoots.put(sentenceName, parseRoot);
            return new ParseTreeGatherer();
        }

        public ParseTreeGatherer setParseTreeRoots(Map<String, ParseState> parseRoots) {
            builder.parseRoots.putAll(parseRoots);
            return new ParseTreeGatherer();
        }

        public RuleConvertorGatherer commitParseTrees() {
            return new RuleConvertorGatherer();
        }

    }

    public class RuleConvertorGatherer {

        public RuleConvertorGatherer setRuleConvertor(String sentenceName, RuleConvertor ruleConvertor) {
            builder.ruleConvertors.put(sentenceName, ruleConvertor);
            return new RuleConvertorGatherer();
        }

        public RuleConvertorGatherer setRuleConversions(Map<String, RuleConvertor> conversions) {
            builder.ruleConvertors.putAll(conversions);
            return new RuleConvertorGatherer();
        }

        public CodeGenerationGatherer commitRuleConvertors() {
            return new CodeGenerationGatherer();
        }

    }

    public class CodeGenerationGatherer {

        public CodeGenerationGatherer setCodeGeneration(String sentenceName, String generatedCode) {
            builder.codeGenerations.put(sentenceName, generatedCode);
            return new CodeGenerationGatherer();
        }

        public CodeGenerationGatherer setCodeGenerations(Map<String, String> generations) {
            builder.codeGenerations.putAll(generations);
            return new CodeGenerationGatherer();
        }

        public TestGrammarFinalizer commitCodeGenerations() {
            return new TestGrammarFinalizer();
        }
        
    }

    public class TestGrammarFinalizer {

        public TestGrammar generateTestGrammar() {
            return new TestGrammar(
                grammar.getTokens(), 
                grammar.getNonTerminals(),
                grammar.getProductionRules(), 
                grammar.getSentinal(), 
                builder.states, 
                builder.actionTable, 
                builder.gotoTable, 
                builder.ruleConvertors, 
                builder.codeGenerations, 
                builder.parseRoots
            );
        }
        
    }

}
