package syntax_analysis.grammar_structure_creation;

import java.util.*;
import java.util.Map.*;

import grammar_objects.*;

public class NoLookaheadState implements State {
    private Set<GrammarPosition> positions;
    private State parentState;

    private Set<Route> branches;

    public NoLookaheadState(Set<GrammarPosition> positions, State parentState) {
        if(positions == null) { positions = new HashSet<>(); }
        
        this.positions = positions;
        this.parentState = parentState;

        branches = new HashSet<>();
    }

    public Set<GrammarPosition> getPositions() {
        return positions;
    }

    public State getParentState() {
        return parentState;
    }

    public State addBranch(Route branch) {
        branches.add(branch);
        return this;
    }

    public Set<Route> getBranches() {
        return branches;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof NoLookaheadState)) { return false; }

        NoLookaheadState otherState = (NoLookaheadState)obj;

        if(positions.size() != otherState.getPositions().size()) { return false; }
        for(GrammarPosition position : positions) {
            if(!otherState.getPositions().contains(position)) { return false; }
        }

        if(branches.size() != otherState.getBranches().size()) { return false; }
            
        return branchesEqual(otherState);
    }

    /**
     * Tests equality of branches between this and another state, based on number of Routes for each LexicalElement
     * @param otherState The other state to be compared with
     * @return The equality of both state's branches
     */
    private boolean branchesEqual(NoLookaheadState otherState) {
        HashMap<LexicalElement, Integer> currentRouteCount = new HashMap<>();
        HashMap<LexicalElement, Integer> otherRouteCount = new HashMap<>();

        for (Route branch : getBranches()) {
            LexicalElement elementTraversed = branch.elementTraversed();

            if(currentRouteCount.get(elementTraversed) != null) {
                currentRouteCount.put(elementTraversed, currentRouteCount.get(elementTraversed) + 1);
            }
            else {
                currentRouteCount.put(elementTraversed, 1);
            }
        }

        for (Route otherBranch : otherState.getBranches()) {
            LexicalElement elementTraversed = otherBranch.elementTraversed();

            if(otherRouteCount.get(elementTraversed) != null) {
                otherRouteCount.put(elementTraversed, otherRouteCount.get(elementTraversed) + 1);
            }
            else {
                otherRouteCount.put(elementTraversed, 1);
            }
        }

        boolean routesEqual = true;
        for(Entry<LexicalElement, Integer> entry : currentRouteCount.entrySet()) {
            if(otherRouteCount.get(entry.getKey()) != entry.getValue()) {
                routesEqual = false;
            }
        }

        return routesEqual;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;

        for(GrammarPosition position : positions) {
            hashCode *= position.hashCode();
        }

        return hashCode;
    }

    @Override
    public String toString() {
        String string = "State:\n";

        for (GrammarPosition position : positions) {
            string += "\t" + position.toString() + "\n";
        }

        string.stripTrailing();
        return string;
    }
}
