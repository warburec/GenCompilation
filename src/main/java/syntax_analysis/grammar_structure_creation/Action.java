package syntax_analysis.grammar_structure_creation;

public interface Action {

    public class UnsuportedActionException extends RuntimeException {
        
        public UnsuportedActionException(String message) {
            super(message);
        }
    }

}