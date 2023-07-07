package syntax_analysis.grammar_structure_creation;

import grammar_objects.Token;

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

        message += ", expected ";

        GrammarPosition[] positions = state.getPositions().toArray(new GrammarPosition[state.getPositions().size()]);

        if(positions.length > 1) {
            for(int i = 0; i < positions.length - 2; i++) {
                message += positions[i].getNextElement().toString();
                
                if(i < positions.length - 3) {
                    message += ", ";
                }
            }
            
            message += " or ";
        }

        message += positions[positions.length - 1].getNextElement().toString();

        return message;
    }
}