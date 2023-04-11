package GrammarObjects;

import java.util.*;

public class State {
    private Set<GrammarPosition> positions;
    private State parentState;

    private Set<Route> treeBranches;
    private Set<Route> graphBranches;

    public State(Set<GrammarPosition> positions, State parentState) {
        if(positions == null) { positions = new HashSet<>(); }
        
        this.positions = positions;
        this.parentState = parentState;

        treeBranches = new HashSet<>();
        graphBranches = new HashSet<>();
    }

    public Set<GrammarPosition> getPositions() {
        return positions;
    }

    public State getParentState() {
        return parentState;
    }

    public Set<Route> getTreeBranches() {
        return treeBranches;
    }

    public Set<Route> getGraphBranches() {
        return graphBranches;
    }

    public State addTreeBranch(Route branch) {
        treeBranches.add(branch);
        return this;
    }

    public State addGraphBranch(Route branch) {
        graphBranches.add(branch);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof State)) { return false; }

        State otherState = (State)obj;

        try {
            if(!parentState.equals(otherState.getParentState())) {
                return false;
            }
        }
        catch(NullPointerException e) {
            if((parentState == null) != (otherState.getParentState() == null)) { return false; }
        }

        if(positions.size() != otherState.getPositions().size()) { return false; }

        for (GrammarPosition position : positions) {
            if(!otherState.getPositions().contains(position)) { return false; }
        }

        if(treeBranches.size() != otherState.getTreeBranches().size()) { return false; }

        for (Route treeReRoute : treeBranches) {
            if(!otherState.getTreeBranches().contains(treeReRoute)) { return false; }
        }

        if(graphBranches.size() != otherState.getGraphBranches().size()) { return false; }

        for (Route graphBranch : graphBranches) {
            if(!otherState.getGraphBranches().contains(graphBranch)) { return false; }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;

        for(GrammarPosition position : positions) {
            hashCode *= position.hashCode();
        }

        if(parentState != null) {
            hashCode *= parentState.hashCode();
        }

        for(Route branch : treeBranches) {
            hashCode *= branch.hashCode();
        }

        hashCode *= graphBranches.size();

        return hashCode;
    }
}
