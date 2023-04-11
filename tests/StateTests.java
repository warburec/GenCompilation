package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import GrammarObjects.GrammarPosition;
import GrammarObjects.LexicalElement;
import GrammarObjects.NonTerminal;
import GrammarObjects.ProductionRule;
import GrammarObjects.State;
import GrammarObjects.Token;

public class StateTests {

    @Test
    public void equalityNullParent() {
        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition position = new GrammarPosition(rule, 0);
        Set<GrammarPosition> positions = Set.of(position);
        State s1 = new State(positions, null);
        State s2 = new State(positions, null);

        assertEquals(s1, s2);
    }

    @Test
    public void equalitySameParent() {
        ProductionRule parentRule = new ProductionRule(new NonTerminal("TestParent"), new LexicalElement[] {new Token("testToken1")});
        GrammarPosition parentPosition = new GrammarPosition(parentRule, 0);
        Set<GrammarPosition> parentPositions = Set.of(parentPosition);
        State parentState = new State(parentPositions, null);

        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken2")});
        GrammarPosition position = new GrammarPosition(rule, 0);
        Set<GrammarPosition> positions = Set.of(position);
        State s1 = new State(positions, parentState);
        State s2 = new State(positions, parentState);

        assertEquals(s1, s2);
    }

    @Test
    public void differentPositions() {
        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition position1 = new GrammarPosition(rule, 0);
        GrammarPosition position2 = new GrammarPosition(rule, 1);
        Set<GrammarPosition> positions1 = Set.of(position1);
        Set<GrammarPosition> positions2 = Set.of(position2);
        State s1 = new State(positions1, null);
        State s2 = new State(positions2, null);

        assertNotEquals(s1, s2);
    }

    @Test
    public void extraPosition() {
        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition position1 = new GrammarPosition(rule, 0);
        GrammarPosition position2 = new GrammarPosition(rule, 1);
        Set<GrammarPosition> positions1 = Set.of(position1);
        Set<GrammarPosition> positions2 = Set.of(position1, position2);
        State s1 = new State(positions1, null);
        State s2 = new State(positions2, null);

        assertNotEquals(s1, s2);
    }

    @Test
    public void setContains() {
        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition position = new GrammarPosition(rule, 0);
        Set<GrammarPosition> positions = Set.of(position);
        State s1 = new State(positions, null);
        State s2 = new State(positions, null);
        Set<State> set = new HashSet<>();
        set.add(s1);

        assertTrue(set.contains(s2));
    }
}
