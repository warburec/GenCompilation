package syntax_analysis.grammar_structure_creation;

import java.util.Set;

public interface State {
    public Set<GrammarPosition> getPositions();
    public State getParentState();
    public State addBranch(Route branch);
    public Set<Route> getBranches();
}
