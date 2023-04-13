package GrammarObjects;

public record Route(State gotoState, LexicalElement elementTraversed) {
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Route)) { return false; }

        Route otherRoute = (Route)obj;

        //TODO: Handle state

        if(!elementTraversed.equals(otherRoute.elementTraversed())) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return elementTraversed.hashCode();
    }
}
