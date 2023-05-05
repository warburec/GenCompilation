package syntax_analysis.grammar_structure_creation;

import grammar_objects.LexicalElement;

public record Route(State gotoState, LexicalElement elementTraversed) {
    
    @Override
    public String toString() {
        return "elementTraversed: " + elementTraversed.toString() + "\n" + gotoState.toString();
    }
}
