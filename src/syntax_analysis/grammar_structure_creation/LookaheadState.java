package syntax_analysis.grammar_structure_creation;

import java.util.*;
import java.util.Map.*;

import grammar_objects.*;
import helperObjects.NotEmptyTuple;

public class LookaheadState implements State {
    private Set<NotEmptyTuple<GrammarPosition, List<LexicalElement>>> positionsAndLookahead;
    private State parentState;

    private Set<GrammarPosition> positions;

    private Set<Route> branches;

    public LookaheadState(Set<NotEmptyTuple<GrammarPosition, List<LexicalElement>>> positionsAndLookahead, State parentState) {
        if(positionsAndLookahead == null) { positionsAndLookahead = new HashSet<>(); }
        
        this.positionsAndLookahead = positionsAndLookahead;
        this.parentState = parentState;

        this.positions = getPositions();

        branches = new HashSet<>();
    }

    public Set<GrammarPosition> getPositions() {
        if(this.positions != null) {
            return this.positions;
        }

        Set<GrammarPosition> positions = new HashSet<>();

        for (NotEmptyTuple<GrammarPosition, List<LexicalElement>> posAndLookahead : positionsAndLookahead) {
            positions.add(posAndLookahead.value1());
        }

        return positions;
    }

    public Set<NotEmptyTuple<GrammarPosition, List<LexicalElement>>> getPositionsAndLookahead() {
        return positionsAndLookahead;
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

        LookaheadState otherState = (LookaheadState)obj;

        if(positionsAndLookahead.size() != otherState.positionsAndLookahead.size()) { return false; }
        for(NotEmptyTuple<GrammarPosition, List<LexicalElement>> posAndLookahead : positionsAndLookahead) {
            if(!otherState.getPositionsAndLookahead().contains(posAndLookahead)) { return false; }
        }

        if(branches.size() != otherState.getBranches().size()) { return false; }
            
        return branchesEqual(otherState);
    }

    /**
     * Tests equality of branches between this and another state, based on number of Routes for each LexicalElement
     * @param otherState The other state to be compared with
     * @return The equality of both state's branches
     */
    private boolean branchesEqual(LookaheadState otherState) {
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

        for(NotEmptyTuple<GrammarPosition, List<LexicalElement>> posAndLookahead : positionsAndLookahead) {
            hashCode *= posAndLookahead.hashCode();
        }

        return hashCode;
    }

    @Override
    public String toString() {
        String string = "State:\n";

        for (NotEmptyTuple<GrammarPosition, List<LexicalElement>> posAndLookahead : positionsAndLookahead) {
            string += "\t" + posAndLookahead.toString() + "\n";
        }

        string.stripTrailing();
        return string;
    }
}
