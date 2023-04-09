package GrammarObjects;

import java.util.List;

public record State(List<GrammarPosition> positions, State parentState) {
    
}
