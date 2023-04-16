package GrammarObjects.GrammarStructureCreation;

import java.util.Map;

import GrammarObjects.Fundamentals.*;

public record ShiftAction(Map<Token, State> shifts) implements Action {
    
}
