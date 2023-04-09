package GrammarObjects;

import java.util.LinkedList;
import java.util.List;

public class State {
    private List<GrammarPosition> positions;
    private State parentState;

    private List<Route> treeBranches;
    private List<Route> graphBranches;

    public State(List<GrammarPosition> positions, State parentState) {
        this.positions = positions;
        this.parentState = parentState;

        treeBranches = new LinkedList<>();
        graphBranches = new LinkedList<>();
    }

    public List<GrammarPosition> getPositions() {
        return positions;
    }

    public State getParentState() {
        return parentState;
    }

    public List<Route> getTreeBranches() {
        return treeBranches;
    }

    public List<Route> getGraphBranches() {
        return graphBranches;
    }

    public void addTreeBranch(Route branch) {
        treeBranches.add(branch);
    }

    public void addGraphBranch(Route branch) {
        graphBranches.add(branch);
    }
}
