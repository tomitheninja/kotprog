package game.payable;

import game.payable.Hero.ShopHero;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HeroTest {

    ShopHero hero;

    @BeforeEach
    void setUp() {
        hero = new ShopHero();
    }

    @Test
    public void testValidEnhanceLevelCost() {
        assertEquals(5, ShopHero.SkillAction.getEnhanceCost(2));
        assertEquals(6, ShopHero.SkillAction.getEnhanceCost(3));
        assertEquals(7, ShopHero.SkillAction.getEnhanceCost(4));
        assertEquals(8, ShopHero.SkillAction.getEnhanceCost(5));
        assertEquals(9, ShopHero.SkillAction.getEnhanceCost(6));
        assertEquals(10, ShopHero.SkillAction.getEnhanceCost(7));
        assertEquals(11, ShopHero.SkillAction.getEnhanceCost(8));
        assertEquals(13, ShopHero.SkillAction.getEnhanceCost(9));
    }

    @Test()
    public void testInvalidEnhanceLevelCost() {
        Assertions.assertThrows((AssertionError.class), () -> {
            ShopHero.SkillAction.getEnhanceCost(-1);
        });
        Assertions.assertThrows((AssertionError.class), () -> {
            ShopHero.SkillAction.getEnhanceCost(0);
        });
        Assertions.assertThrows((AssertionError.class), () -> {
            ShopHero.SkillAction.getEnhanceCost(1);
        });
    }

    @Test
    public void testRandom() {
        for (int i = 0; i < 100; i++) {
            Hero h = new Hero();
            assertTrue(1 <= h.attack);
            assertTrue(h.attack <= 10);
        }
    }
}