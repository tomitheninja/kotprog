package game.payable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitTest {

    Unit.SpecialAction emptyAction = new Unit.SpecialAction() {
        @Override
        public void action() {
        }
    };

    @Test
    void getInitialHealth() {
        int INIT_HEALTH = 100;
        Unit unit = new Unit(Unit.Type.FOLDMUVES, 1, 1, INIT_HEALTH, 1, 1, emptyAction, null);
        assertEquals(INIT_HEALTH, unit.getHealth());
        assertTrue(unit.isAlive());
    }

    @Test
    void takeDamage() {
        Unit unit = new Unit(Unit.Type.FOLDMUVES, 1, 1, 5, 1, 1, emptyAction, null);
        assertEquals(5, unit.getHealth());

        // take 4 damage. health should be 1
        assertFalse(unit.takeDamage(4));
        assertTrue(unit.isAlive());
        assertEquals(1, unit.getHealth());

        // take 2 damage. health should be 0
        assertTrue(unit.takeDamage(2));
        assertFalse(unit.isAlive());
        assertEquals(0, unit.getHealth());
    }

    @Test
    void testAttack() {
        final int INIT_MIN_ATTACK = 100;
        final int INIT_MAX_ATTACK = 200;
        final int INIT_HEALTH = 10;
        Unit unit = new Unit(Unit.Type.GRIFF, INIT_MIN_ATTACK, INIT_MAX_ATTACK, INIT_HEALTH, 1, 1, emptyAction, null);
        for (int i = 0; i < 100; i++) {
            int attack = unit.getAttack();
            assertTrue(INIT_MIN_ATTACK <= attack && attack <= INIT_MAX_ATTACK);
        }

        unit.takeDamage(INIT_HEALTH / 2);
        for (int i = 0; i < 100; i++) {
            int attack = unit.getAttack();
            assertTrue(INIT_MIN_ATTACK / 2 <= attack && attack <= INIT_MAX_ATTACK / 2);
        }

    }
}