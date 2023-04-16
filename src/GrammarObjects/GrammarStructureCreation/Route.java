package GrammarObjects.GrammarStructureCreation;

import GrammarObjects.Fundamentals.LexicalElement;

public record Route(State gotoState, LexicalElement elementTraversed) {
    
}
