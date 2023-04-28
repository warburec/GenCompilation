package syntax_analysis.grammar_structure_creation;

import java.util.*;

public class State {
    private Set<GrammarPosition> positions;
    private State parentState;

    private Set<Route> branches;

    public State(Set<GrammarPosition> positions, State parentState) {
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
        if(!(obj instanceof State)) { return false; }

        State otherState = (State)obj;

        if(positions.size() != otherState.getPositions().size()) { return false; }
        for(GrammarPosition position : positions) {
            if(!otherState.getPositions().contains(position)) { return false; }
        }

        if(branches.size() != otherState.getBranches().size()) { return false; }
        for(Route branch : branches) {
            boolean matchFound = false;

            for (Route otherBranch : otherState.getBranches()) {
                if(branch.elementTraversed().equals(otherBranch.elementTraversed())) { 
                    matchFound = true; 
                    break;
                }
            }

            if(!matchFound) { return false; }
            
            //if(!otherState.getBranches().contains(branch)) { return false; } //TODO: Check how branches are handled for equality, this was an old and failing recursive check
        }
        //TODO handle self referential states

        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;

        for(GrammarPosition position : positions) {
            hashCode *= position.hashCode();
        }

        return hashCode;
    }
}
