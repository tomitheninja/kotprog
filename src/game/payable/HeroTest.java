package game.payable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HeroTest {

    Hero hero;

    @BeforeEach
    void setUp() {
        hero = new Hero();
    }

    @Test
    public void testValidEnhanceLevelCost() {
        assertEquals(5, Hero.SkillAction.getEnhanceCost(2));
        assertEquals(6, Hero.SkillAction.getEnhanceCost(3));
        assertEquals(7, Hero.SkillAction.getEnhanceCost(4));
        assertEquals(8, Hero.SkillAction.getEnhanceCost(5));
        assertEquals(9, Hero.SkillAction.getEnhanceCost(6));
        assertEquals(10, Hero.SkillAction.getEnhanceCost(7));
        assertEquals(11, Hero.SkillAction.getEnhanceCost(8));
        assertEquals(13, Hero.SkillAction.getEnhanceCost(9));
    }

    @Test()
    public void testInvalidEnhanceLevelCost() {
        Assertions.assertThrows((AssertionError.class), () -> {
            Hero.SkillAction.getEnhanceCost(-1);
        });
        Assertions.assertThrows((AssertionError.class), () -> {
            Hero.SkillAction.getEnhanceCost(0);
        });
        Assertions.assertThrows((AssertionError.class), () -> {
            Hero.SkillAction.getEnhanceCost(1);
        });
    }
}