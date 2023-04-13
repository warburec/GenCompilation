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

    public Set<Route> getBranches() {
        Set<Route> branches = new HashSet<>(treeBranches);
        branches.addAll(graphBranches);
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

        // if(treeBranches.size() != otherState.getTreeBranches().size()) { return false; } //TODO: Check all outbound links as one set (not as seperate branch sets)
        // for(Route treeRoute : treeBranches) {                                                // Maybe only check the elementTraversed on the routes and not the states they end at
        //     if(!otherState.getTreeBranches().contains(treeRoute)) { return false; }
        // }

        // if(graphBranches.size() != otherState.getGraphBranches().size()) { return false; }
        // for(Route graphBranch : graphBranches) {
        //     if(!otherState.getGraphBranches().contains(graphBranch)) { return false; }
        // }

        Set<Route> branches = getBranches();
        if(branches.size() != otherState.getBranches().size()) { return false; }
        for(Route branch : branches) {
            if(!otherState.getBranches().contains(branch)) { return false; }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1; //TODO make better hash

        // for(GrammarPosition position : positions) {
        //     hashCode *= position.hashCode();
        // }

        return hashCode;
    }
}
