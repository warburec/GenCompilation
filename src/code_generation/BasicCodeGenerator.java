package code_generation;

import java.util.*;

import grammar_objects.ProductionRule;
import syntax_analysis.parsing.*;

public class BasicCodeGenerator implements CodeGenerator {
    protected Map<ProductionRule, Generator> ruleConvertor;

    public BasicCodeGenerator(Map<ProductionRule, Generator> ruleConvertor) {
        this.ruleConvertor = ruleConvertor;
    }

    @Override
    public String generate(ParseState parseRoot) {
        Stack<ParseState> states = new Stack<>();
        states.add(parseRoot);

        //TODO
        return null;
    }

    private String produceString(GenerationState generationState) {
        ParseState parseState = generationState.parseState;
        
        if(parseState instanceof ShiftedState) {
            ShiftedState shift = (ShiftedState)parseState;
            return shift.tokenUsed().toString(); //TODO: look this up in tables, to allow for identifiers
        }
        else if (parseState instanceof ReducedState) {
            ReducedState reduce = (ReducedState)parseState;

            Generator generator = ruleConvertor.get(reduce.reductionRule());
            return generator.generateCode(generationState.getElements());
        }
        else {
            //TODO make custom exception
            throw new RuntimeException("A parse state was found with the unrecognised type " + parseState.getClass());
        }
    }

    private class GenerationState {
        private ParseState parseState;
        private List<String> elements;

        public GenerationState(ParseState parseState) {
            this.parseState = parseState;
            elements = new ArrayList<>();
        }

        public void addElementString(String element) {
            elements.add(element);
        }

        public String[] getElements() {
            return elements.toArray(new String[elements.size()]);
        }

        public ParseState getParseState() {
            return parseState;
        }
    }

}
