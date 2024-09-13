package syntax_analysis.grammar_structure_creation;

import java.util.*;

import grammar_objects.*;

public class SyntaxError extends UnsupportedOperationException {

    private Token token;
    private State state;

    public SyntaxError(Token token, State state) {
        super();

        this.token = token;
        this.state = state;
    }

    @Override
    public String getMessage() {
        String message;

        message = "Unexpected token \"" + token.getName() + "\"";

        String tokenPosition = token.getPositionString();

        if(tokenPosition != null) {
            message += " at " + tokenPosition.toString();
        }

        message += ". Expected ";

        Set<LexicalElement> nextElements =  gatherUniqueNextElements(state);
        Iterator<LexicalElement> nextElemIterator = nextElements.iterator();

        message += nextElemIterator.next().toString();

        if (nextElements.size() == 1) { return message; }

        while (nextElemIterator.hasNext()) {
            LexicalElement element = nextElemIterator.next();
            
            if (nextElemIterator.hasNext()) {
                message += ", ";
            }
            else {
                message += " or ";
            }

            message += element.toString();
        }

        return message;
    }

    private Set<LexicalElement> gatherUniqueNextElements(State state) {
        Set<LexicalElement> nextElements = new HashSet<>();

        GrammarPosition[] positions = state.getPositions().toArray(new GrammarPosition[state.getPositions().size()]);

        for (GrammarPosition position : positions) {
            nextElements.add(position.getNextElement());
        }

        return nextElements;
    }
}