package syntax_analysis.grammar_structure_creation;

public record Accept() implements Action {
    
    @Override
    public final String toString() {
        return "ACCEPT";
    }

}
