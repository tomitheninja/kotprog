package game.payable;

import java.util.Random;

public class Unit {
    private static final Random random = new Random();
    private static final Unit Foldmuves = new Unit(Type.FOLDMUVES, 1, 1, 3, 4, 8, null);
    private static final Unit Ijasz = new Unit(Type.IJASZ, 2, 4, 7, 4, 9, null);
    private static final Unit Griff = new Unit(Type.GRIFF, 5, 10, 7, 15, 30, null);
    private static final SpecialAction emptySpecialAction = new SpecialAction() {
        @Override
        public void action() {

        }
    };
    public final Type type;
    public final int minAttack;
    public final int maxAttack;
    public final int maxHealth;
    public final int movement;
    public final int initiative;
    public final SpecialAction specialAction;
    protected int health;

    Unit(Type type, int minAttack, int maxAttack, int maxHealth, int movement, int initiative, SpecialAction specialAction) {
       /* if (type == null) throw new IllegalArgumentException("Type cannot be null");
        if (minAttack <= 0) throw new IllegalArgumentException("minAttack must be positive");
        if (maxAttack < minAttack) throw new IllegalArgumentException("maxAttack must be >= to minAttack");
        if (maxHealth <= 0) throw new IllegalArgumentException("maxHealth must be positive");
        if (movement <= 0) throw new IllegalArgumentException("movement must be positive");
        if (initiative <= 0) throw new IllegalArgumentException("initiative must be positive");
        if (specialAction == null) throw new IllegalArgumentException("specialAction cannot be null");*/
        this.type = type;
        this.minAttack = minAttack;
        this.maxAttack = maxAttack;
        this.maxHealth = maxHealth;
        this.movement = movement;
        this.initiative = initiative;
        this.health = maxHealth;
        this.specialAction = specialAction != null ? specialAction : emptySpecialAction;
    }

    public Unit(Type type, int amount) {
        this(timesUnit(type.getUnit(), amount));
    }

    public Unit(Unit u) {
        this(u.type, u.minAttack, u.maxAttack, u.maxHealth, u.movement, u.initiative, u.specialAction);
    }

    private static Unit timesUnit(Unit u, int amount) {
        return new Unit(u.type, u.minAttack * amount, u.maxAttack * amount, u.maxHealth * amount, u.movement, u.initiative, u.specialAction);
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

        private Unit getUnit() {
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
