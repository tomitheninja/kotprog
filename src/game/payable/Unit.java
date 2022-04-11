package game.payable;

import java.util.Random;

public class Unit {

    private static final Random random = new Random();
    public final String name;
    public final int minAttack;
    public final int maxAttack;
    public final int maxHealth;
    public final int movement;
    public final int initiative;
    public final SpecialAction specialAction;
    protected int health;

    public Unit(Unit unit) {
        this(unit.name, unit.minAttack, unit.maxAttack, unit.maxHealth, unit.movement, unit.initiative, unit.specialAction);
    }

    public Unit(String name, int minAttack, int maxAttack, int maxHealth, int movement, int initiative, SpecialAction specialAction) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("name cannot be null or empty");
        if (minAttack <= 0) throw new IllegalArgumentException("minAttack must be positive");
        if (maxAttack < minAttack) throw new IllegalArgumentException("maxAttack must be >= to minAttack");
        if (maxHealth <= 0) throw new IllegalArgumentException("maxHealth must be positive");
        if (movement <= 0) throw new IllegalArgumentException("movement must be positive");
        if (initiative <= 0) throw new IllegalArgumentException("initiative must be positive");
        if (specialAction == null) throw new IllegalArgumentException("specialAction cannot be null");
        this.name = name;
        this.minAttack = minAttack;
        this.maxAttack = maxAttack;
        this.maxHealth = maxHealth;
        this.movement = movement;
        this.initiative = initiative;
        this.health = maxHealth;
        this.specialAction = specialAction;
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

    /**
     * @return a random attack value between minAttack * hpPercent and maxAttack * hpPercent
     */
    public int getAttack() {
        int initAttack = random.nextInt(minAttack, maxAttack + 1);
        double attack = (double) health * initAttack / maxHealth;
        return (int) Math.ceil(attack);
    }

    public interface SpecialAction {
        void action();
    }

}
