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
}
