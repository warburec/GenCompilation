package grammar_objects.grammar_structure_creation;

import grammar_objects.fundamentals.LexicalElement;

public record Route(State gotoState, LexicalElement elementTraversed) {
    
}
