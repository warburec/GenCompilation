package GrammarObjects;

public record Route(State gotoState, LexicalElement elementTraversed) {
    
    @Override
    public int hashCode() {
        return elementTraversed.hashCode();
    }
}
