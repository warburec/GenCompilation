package syntax_analysis.grammar_structure_creation;

import java.util.Set;
import grammar_objects.*;

public class CLR1Position extends GrammarPosition {
        Set<Token> followSet;

        /**
         * 
         * @param rule
         * @param position
         * @param followSet The follow set for the rule's NonTerminal given the position this position was derived from
         */
        public CLR1Position(ProductionRule rule, int position, Set<Token> followSet) {
            super(rule, position);
            
            this.followSet = followSet;
        }

        public Set<Token> getFollowSet() {
            return followSet;
        }

        @Override
        public GrammarPosition getNextPosition() {
            return new CLR1Position(rule, position + 1, followSet);
        }
    
        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof CLR1Position)) { return false; }

            CLR1Position otherState = (CLR1Position)obj;
            Set<Token> otherFollowSet = otherState.followSet;

            if(followSet.size() != otherFollowSet.size()) { return false; }

            for(Token followSet : followSet) {
                if(!otherFollowSet.contains(followSet)) { return false; }
            }

            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            int hashCode = super.hashCode();

            for (Token token : followSet) {
                hashCode *= token.hashCode();
            }

            return hashCode;
        }

        @Override
        public String toString() {
            String string = super.toString();

            string += " : ";

            for(Token token : followSet) {
                string += token.toString() + "/";
            }

            return string.substring(0, string.length() - 1);
        }
    }
