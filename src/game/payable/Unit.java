package game.payable;

import javax.swing.*;
import java.util.Objects;
import java.util.Random;

public class Unit {
    private static final Random random = new Random();
    private static final Unit Foldmuves = new Unit(Type.FOLDMUVES, 1, 1, 3, 4, 8, null, new ImageIcon(Objects.requireNonNull(Unit.class.getClassLoader().getResource("falusi_balra.png"))));
    private static final Unit Ijasz = new Unit(Type.IJASZ, 2, 4, 7, 4, 9, null, new ImageIcon(Objects.requireNonNull(Unit.class.getClassLoader().getResource("varazslo_balra.png"))));
    private static final Unit Griff = new Unit(Type.GRIFF, 5, 10, 30, 7, 15, null, new ImageIcon(Objects.requireNonNull(Unit.class.getClassLoader().getResource("griff_balra.png"))));
    private static final SpecialAction emptySpecialAction = new SpecialAction() {
        @Override
        public void action() {
            System.out.println("Empty special action");
        }
    };

    public final ImageIcon img;
    public final Type type;
    public final int minAttack;
    public final int maxAttack;
    public final int maxHealth;
    public final int movement;
    public final int initiative;
    public final SpecialAction specialAction;
    protected int health;

    Unit(Type type, int minAttack, int maxAttack, int maxHealth, int movement, int initiative, SpecialAction specialAction, ImageIcon img) {
        this.type = type;
        this.minAttack = minAttack;
        this.maxAttack = maxAttack;
        this.maxHealth = maxHealth;
        this.movement = movement;
        this.initiative = initiative;
        this.health = maxHealth;
        this.specialAction = specialAction != null ? specialAction : emptySpecialAction;
        this.img = img;
    }

    public Unit(Type type, int amount) {
        this(timesUnit(type.asUnit(), amount));
        if (amount < 1) throw new IllegalArgumentException("amount must be positive");
    }

    public Unit(Unit u) {
        this(u.type, u.minAttack, u.maxAttack, u.maxHealth, u.movement, u.initiative, u.specialAction, u.img);
        this.health = u.maxHealth; // Hide the warning
    }

    /**
     * Refactored to avoid error
     */
    private static Unit timesUnit(Unit u, int amount) {
        return new Unit(u.type, u.minAttack * amount, u.maxAttack * amount, u.maxHealth * amount, u.movement, u.initiative, u.specialAction, u.img);
    }

    @Override
    public String toString() {
        return type + " with health: " + health + "/" + maxHealth + ". Attack:" + getMinAttack() + (getMinAttack() != getMaxAttack() ? "-" + getMaxAttack() : "") + ". Has initiative of " + initiative;
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Decreases the health of the unit by the given amount.
     *
     * @param damage positive amount of damage to deal
     * @return true if the unit is killed, false otherwise
     */
    public boolean takeDamage(int damage) {
        if (damage < 0) throw new IllegalArgumentException("damage must not be negative");
        health = Math.max(0, health - damage);
        return !isAlive();

    }

    public int getMaxAttack() {
        return (int) Math.ceil((double) health * maxAttack / maxHealth);
    }

    public int getMinAttack() {
        return (int) Math.ceil((double) health * minAttack / maxHealth);
    }

    /**
     * @return a random attack value between minAttack * hpPercent and maxAttack * hpPercent
     */
    public int getAttack() {
        return random.nextInt(getMinAttack(), getMaxAttack() + 1);
    }

    public enum Type {
        FOLDMUVES, IJASZ, GRIFF;

        @Override
        public String toString() {
            return switch (this) {
                case FOLDMUVES -> "Földműves";
                case IJASZ -> "Íjász";
                case GRIFF -> "Griff";
            };
        }

        private Unit asUnit() {
            return switch (this) {
                case FOLDMUVES -> Foldmuves;
                case IJASZ -> Ijasz;
                case GRIFF -> Griff;
            };
        }
    }

    public interface SpecialAction {
        void action();
    }

}
