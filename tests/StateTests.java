package tests;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;

public class StateTests {

    @Test
    public void equalityNullParent() {
        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition position = new GrammarPosition(rule, 0);
        Set<GrammarPosition> positions = Set.of(position);
        NoLookaheadState s1 = new NoLookaheadState(positions, null);
        NoLookaheadState s2 = new NoLookaheadState(positions, null);

        assertEquals(s1, s2);
    }

    @Test
    public void equalitySameRuleObj() {
        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition position = new GrammarPosition(rule, 0);
        Set<GrammarPosition> positions = Set.of(position);
        NoLookaheadState s1 = new NoLookaheadState(positions, null);
        NoLookaheadState s2 = new NoLookaheadState(positions, null);

        assertEquals(s1, s2);
    }

    @Test
    public void equalityDifferentRuleObj() {
        ProductionRule rule1 = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition position1 = new GrammarPosition(rule1, 0);
        Set<GrammarPosition> positions1 = Set.of(position1);
        ProductionRule rule2 = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition position2 = new GrammarPosition(rule2, 0);
        Set<GrammarPosition> positions2 = Set.of(position2);
        NoLookaheadState s1 = new NoLookaheadState(positions1, null);
        NoLookaheadState s2 = new NoLookaheadState(positions2, null);

        assertEquals(s1, s2);
    }

    @Test
    public void equalitySameParent() {
        ProductionRule parentRule = new ProductionRule(new NonTerminal("TestParent"), new LexicalElement[] {new Token("testToken1")});
        GrammarPosition parentPosition = new GrammarPosition(parentRule, 0);
        Set<GrammarPosition> parentPositions = Set.of(parentPosition);
        NoLookaheadState parentState = new NoLookaheadState(parentPositions, null);

        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken2")});
        GrammarPosition position = new GrammarPosition(rule, 0);
        Set<GrammarPosition> positions = Set.of(position);
        NoLookaheadState s1 = new NoLookaheadState(positions, parentState);
        NoLookaheadState s2 = new NoLookaheadState(positions, parentState);

        assertEquals(s1, s2);
    }

    @Test
    public void differentPositions() {
        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition position1 = new GrammarPosition(rule, 0);
        GrammarPosition position2 = new GrammarPosition(rule, 1);
        Set<GrammarPosition> positions1 = Set.of(position1);
        Set<GrammarPosition> positions2 = Set.of(position2);
        NoLookaheadState s1 = new NoLookaheadState(positions1, null);
        NoLookaheadState s2 = new NoLookaheadState(positions2, null);

        assertNotEquals(s1, s2);
    }

    @Test
    public void extraPosition() {
        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition position1 = new GrammarPosition(rule, 0);
        GrammarPosition position2 = new GrammarPosition(rule, 1);
        Set<GrammarPosition> positions1 = Set.of(position1);
        Set<GrammarPosition> positions2 = Set.of(position1, position2);
        NoLookaheadState s1 = new NoLookaheadState(positions1, null);
        NoLookaheadState s2 = new NoLookaheadState(positions2, null);

        assertNotEquals(s1, s2);
    }

    @Test
    public void setContains() {
        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition position = new GrammarPosition(rule, 0);
        Set<GrammarPosition> positions = Set.of(position);
        NoLookaheadState s1 = new NoLookaheadState(positions, null);
        NoLookaheadState s2 = new NoLookaheadState(positions, null);
        Set<NoLookaheadState> set = new HashSet<>();
        set.add(s1);

        assertTrue(set.contains(s2));
    }

}
