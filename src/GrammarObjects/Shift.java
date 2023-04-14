package GrammarObjects;

import java.util.Map;

public record Shift(Map<Token, State> shifts) implements Action {
    
}
