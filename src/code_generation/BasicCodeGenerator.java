package code_generation;

import java.util.*;

import grammar_objects.*;
import helperObjects.NullableTuple;
import syntax_analysis.parsing.*;

public class BasicCodeGenerator implements CodeGenerator {
    public static ProductionRule ROOT_RULE = null;

    protected Map<ProductionRule, Generator> ruleConvertor;
    protected String preGeneration;
    protected String postGeneration;

    public BasicCodeGenerator(RuleConvertor ruleConvertor) {
        this.ruleConvertor = ruleConvertor.getConversions();

        NullableTuple<String, String> bookends = ruleConvertor.getBookends();

        this.preGeneration = bookends.value1();
        this.postGeneration = bookends.value2();
    }

    /**
     * 
     * @param ruleConvertorMap A map of production rules and generation functions to produce strings for them, map ROOT_RULE to a Generator function to alter the entire generated code
     * @param preGeneration A string that will precede the generated code
     * @param postGeneration A string that will follow the generated code
     */
    public BasicCodeGenerator(Map<ProductionRule, Generator> ruleConvertorMap, String preGeneration, String postGeneration) {
        this.ruleConvertor = ruleConvertorMap;
        this.preGeneration = preGeneration;
        this.postGeneration = postGeneration;
    }

    @Override
    public String generate(ParseState parseRoot) {
        Stack<GenerationState> states = new Stack<>();
        states.push(new GenerationState(parseRoot));

        CodeElement currentlyBuiltCode = null;
        while(!states.isEmpty()) {
            GenerationState currentState = states.peek();

            if(currentState.getParseState() instanceof ShiftedState) {
                currentlyBuiltCode = produceShiftedString((ShiftedState)currentState.getParseState());
                    
                states.pop();

                if(!states.isEmpty()) {
                    GenerationState parentState = states.peek();
                    parentState.addCodeElement(currentlyBuiltCode);
                }
            }
            else if(currentState.getParseState() instanceof ReducedState) {
                ReducedState reduction = (ReducedState)currentState.getParseState();

                boolean stateGenerationComplete = currentState.getElementsGenerated() == reduction.statesReduced().size();
                if(stateGenerationComplete) {
                    currentlyBuiltCode = produceReductionString(currentState);
                    
                    states.pop();

                    if(!states.isEmpty()) {
                        GenerationState parentState = states.peek();
                        parentState.addCodeElement(currentlyBuiltCode);
                    }
                }
                else {
                    ParseState nthState = reduction.statesReduced().get(currentState.getElementsGenerated());
                    states.push(new GenerationState(nthState));
                }
            }
            else {
                if(currentState.getParseState() == null) {
                    states.pop(); //Goto the incomplete parent state
                    throw new IncompleteReductionException(states.peek());
                }

                throw new UnrecognisedParseStateException(currentState.getParseState());
            }
        }

        Generator rootGenerator = ruleConvertor.get(ROOT_RULE);
        if(rootGenerator != null) {
            currentlyBuiltCode = new NonTerminalGeneration(rootGenerator.generateCode(new CodeElement[] { currentlyBuiltCode }));
        }

        return preGeneration + currentlyBuiltCode.getGeneration() + postGeneration;
    }

    private CodeElement produceShiftedString(ShiftedState shift) {
        Token token = shift.tokenUsed();

        if(token instanceof Identifier) {
            return new IdentifierGeneration((Identifier)token);
        }
        if(token instanceof Literal) {
            return new LiteralGeneration((Literal)token);
        }
        else {
            return new TokenGeneration(token);
        }
    }

    private CodeElement produceReductionString(GenerationState reductionGeneration) {
        ReducedState reduction = null;

        try {
            reduction = (ReducedState)reductionGeneration.getParseState();
        }
        catch(ClassCastException e) {
            throw new RuntimeException("Reduction string generation was attempted for ParseState of type " + reductionGeneration.getParseState().getClass().getName());
        }

        Generator generator = ruleConvertor.get(reduction.reductionRule());
        return new NonTerminalGeneration(generator.generateCode(reductionGeneration.getCodeElements()));
    }

    public class IncompleteReductionException extends RuntimeException {
        public IncompleteReductionException(GenerationState incompleteState) {
            super("Code generation for a reduction with incomplete elements was attempted, check the parse tree is correctly generated\n" +
                incompleteState.toString());
        }
    }

    private class GenerationState {
        private ParseState parseState;
        private List<CodeElement> codeElements;
        private int elementsGenerated;

        public GenerationState(ParseState parseState) {
            this.parseState = parseState;
            codeElements = new ArrayList<>();
            elementsGenerated = 0;
        }

        public void addCodeElement(CodeElement element) {
            codeElements.add(element);
            elementsGenerated++;
        }

        public CodeElement[] getCodeElements() {
            return codeElements.toArray(new CodeElement[codeElements.size()]);
        }

        public ParseState getParseState() {
            return parseState;
        }

        public int getElementsGenerated() {
            return elementsGenerated;
        }

        @Override
        public String toString() {
            String string = "";

            string += "parseState: ";
            if(parseState == null) {
                string += null;
            }
            else {
                string += parseState.toString();
            }
            string += "\n";

            string += "elementsGenerated: " + elementsGenerated;

            return string;
        }
    }

}
