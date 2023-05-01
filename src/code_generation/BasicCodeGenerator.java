package code_generation;

import java.util.*;

import grammar_objects.*;
import syntax_analysis.parsing.*;

public class BasicCodeGenerator implements CodeGenerator {
    protected Map<ProductionRule, Generator> ruleConvertor;
    protected String preGeneration;
    protected String postGeneration;

    protected Set<IdentifierGeneration> declaredIdentifiers;

    public BasicCodeGenerator(Map<ProductionRule, Generator> ruleConvertor, String preGeneration, String postGeneration) {
        this.ruleConvertor = ruleConvertor;
        this.preGeneration = preGeneration;
        this.postGeneration = postGeneration;

        this.declaredIdentifiers = new HashSet<>();
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
                    throw new IncompleteReductionException();
                }

                throw new UnrecognisedParseStateException(currentState.getParseState());
            }
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
        return new NonTerminalGeneration(generator.generateCode(this, reductionGeneration.getCodeElements()));
    }

    public boolean isDeclared(IdentifierGeneration identifier) {
        return declaredIdentifiers.contains(identifier);
    }

    public void setDeclared(IdentifierGeneration identfier) {
        declaredIdentifiers.add(identfier);
    }

    public class IncompleteReductionException extends RuntimeException {
        public IncompleteReductionException() {
            super("Code generation for a reduction with incomplete elements was attempted, check the parse tree is correctly generated");
            //TODO: More descriptive message, highlight the state with issues
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

    }

}
