package tests;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import GrammarObjects.GrammarPosition;
import GrammarObjects.LexicalElement;
import GrammarObjects.NonTerminal;
import GrammarObjects.ProductionRule;
import GrammarObjects.Token;

public class GrammarPositionTests {
    
    @Test
    public void equality() {
        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition gp1 = new GrammarPosition(rule, 0);
        GrammarPosition gp2 = new GrammarPosition(rule, 0);

        assertEquals(gp1, gp2);
    }

    @Test
    public void differentPosition() {
        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition gp1 = new GrammarPosition(rule, 0);
        GrammarPosition gp2 = new GrammarPosition(rule, 1);

        assertNotEquals(gp1, gp2);
    }

    @Test
    public void differentRule() {
        ProductionRule rule1 = new ProductionRule(new NonTerminal("Test1"), new LexicalElement[] {new Token("testToken1")});
        ProductionRule rule2 = new ProductionRule(new NonTerminal("Test2"), new LexicalElement[] {new Token("testToken2")});
        GrammarPosition gp1 = new GrammarPosition(rule1, 0);
        GrammarPosition gp2 = new GrammarPosition(rule2, 0);

        assertNotEquals(gp1, gp2);
    }

    @Test
    public void setContains() {
        ProductionRule rule = new ProductionRule(new NonTerminal("Test"), new LexicalElement[] {new Token("testToken")});
        GrammarPosition gp1 = new GrammarPosition(rule, 0);
        GrammarPosition gp2 = new GrammarPosition(rule, 0);
        Set<GrammarPosition> set = new HashSet<>();
        set.add(gp1);

        assertTrue(set.contains(gp2));
    }
    
}
