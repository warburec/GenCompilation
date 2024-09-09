package code_generation;

import grammar_objects.DynamicToken;

public class DynamicTokenGeneration implements CodeElement {

    private DynamicToken dynamicToken;

    public DynamicTokenGeneration(DynamicToken token) {
        this.dynamicToken = token;
    }

    @Override
    public String getGeneration() {
        return dynamicToken.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DynamicTokenGeneration)) { return false; }

        DynamicTokenGeneration otherGeneration = (DynamicTokenGeneration)obj;
        return dynamicToken.exactlyEquals(otherGeneration.dynamicToken);
    }

    @Override
    public int hashCode() {
        return dynamicToken.hashCode();
    }
}
